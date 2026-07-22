package com.foodorder.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.enums.OrderLogAction;
import com.foodorder.enums.PaymentStatus;
import com.foodorder.enums.PaymentType;
import com.foodorder.exception.CartNotFoundException;
import com.foodorder.exception.DeliveryBoyNotAvailableException;
import com.foodorder.exception.OrderNotFoundException;
import com.foodorder.model.*;
import com.foodorder.repository.*;
import com.foodorder.state.CancelledState;
import com.foodorder.state.OrderState;
import com.foodorder.state.PlacedState;
import com.foodorder.util.FileUtil;
import com.foodorder.util.OTPGenerator;

public class OrderService {
    private final OrderRepository orderRepository = new OrderRepository();
    private final OrderItemRepository orderItemRepository = new OrderItemRepository();
    private final CartRepository cartRepository = new CartRepository();
    private final CartItemRepository cartItemRepository = new CartItemRepository();
    private final MenuItemRepository menuItemRepository = new MenuItemRepository();

    public Order placeOrder(String customerId, String deliveryAddressId, PaymentType paymentType, double deliveryCharge) {
        Cart cart = cartRepository.findByCustomerId(customerId);

        if (cart == null) {
            throw new CartNotFoundException(MessageConstants.CART_NOT_FOUND);
        }

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        double subtotal = 0;
        double totalDiscount = 0;

        for (CartItem cartItem : cartItems) {
            MenuItem menuItem = menuItemRepository.findById(cartItem.getMenuItemId());
            totalDiscount += (menuItem.getPrice() * menuItem.getDiscount()) / 100;
            double priceAfterDiscount = menuItem.getPrice() - ((menuItem.getPrice() * menuItem.getDiscount()) / 100);
            subtotal += priceAfterDiscount * cartItem.getQuantity();
        }

        Order order = new Order(
                customerId,
                cart.getRestaurantId(),
                null,
                LocalDateTime.now(),
                subtotal,
                totalDiscount,
                deliveryCharge,
                paymentType,
                null
        );

        order.setDeliveryAddressId(deliveryAddressId);
        order.setOrderState(new PlacedState());

        orderRepository.save(order);

        PaymentStatus paymentStatus = (paymentType == PaymentType.CASH) ? PaymentStatus.PENDING: PaymentStatus.SUCCESS;

        Payment payment = new Payment(
                order.getId(),
                subtotal + deliveryCharge,
                paymentType,
                paymentStatus
        );

        payment = new PaymentService().createPayment(payment);

        String otp = OTPGenerator.generateOTP();

        new OrderOTPRepository().save(
                new OrderOTP(
                        order.getId(),
                        otp,
                        LocalDateTime.now()
                )
        );

        new OrderLogService().addLog(
                new OrderLog(
                        order.getId(),
                        OrderLogAction.ORDER_PLACED,
                        customerId,
                        LocalDateTime.now()
                )
        );

        for (CartItem cartItem : cartItems) {
            MenuItem menuItem = menuItemRepository.findById(cartItem.getMenuItemId());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setMenuItemId(menuItem.getId());
            orderItem.setPrice(menuItem.getPrice());
            orderItem.setDiscount(menuItem.getDiscount());
            orderItem.setQuantity(cartItem.getQuantity());

            orderItemRepository.save(orderItem);
        }

        cartItemRepository.deleteByCartId(cart.getId());
        cartRepository.deleteById(cart.getId());

        return order;
    }

    public void updateOrderStatus(Order order, OrderState newState) {
        order.setOrderState(newState);
        orderRepository.update(order);
    }

    public void assignDeliveryBoy(String orderId) {
        Order order = orderRepository.findById(orderId);

        if (order == null) {
            throw new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND);
        }

        String restaurantId = order.getRestaurantId();

        List<User> deliveryBoys = new UserRepository().findAllDeliveryBoys();

        List<Order> allOrders = orderRepository.findAll();

        List<String> eligibleBoys = new ArrayList<>();

        for (User boy : deliveryBoys) {
            // count active orders of this boy for SAME RESTAURANT only
            long activeOrders = allOrders.stream()
                    .filter(o ->
                            o.getDeliveryBoyId() != null
                                    && o.getDeliveryBoyId().equals(boy.getId())
                                    && o.getRestaurantId().equals(restaurantId)
                                    && !o.getOrderState().toString().equals("DELIVERED")
                    )
                    .count();

            if (activeOrders >= 3) {
                continue;
            }

            eligibleBoys.add(boy.getId());
        }

        if (eligibleBoys.isEmpty()) {
            throw new DeliveryBoyNotAvailableException(MessageConstants.DELIVERY_BOY_NOT_AVAILABLE);
        }

        String selectedBoyId = eligibleBoys.get(
                new java.util.Random().nextInt(eligibleBoys.size())
        );

        order.setDeliveryBoyId(selectedBoyId);

        orderRepository.update(order);
    }

    public void acceptOrder(String orderId, String restaurantOwnerId) {
        Order order = orderRepository.findById(orderId);

        order.getOrderState().next(order); // PLACED -> PREPARING

        orderRepository.update(order);

        new OrderLogService().addLog(
                new OrderLog(
                        orderId,
                        OrderLogAction.ORDER_PREPARING,
                        restaurantOwnerId,
                        LocalDateTime.now()
                )
        );
    }

    public void markOrderReady(String orderId, String restaurantOwnerId) {
        Order order = orderRepository.findById(orderId);

        order.getOrderState().next(order); // PREPARING -> READY

        orderRepository.update(order);

        new OrderLogService().addLog(
                new OrderLog(
                        orderId,
                        OrderLogAction.ORDER_READY,
                        restaurantOwnerId,
                        LocalDateTime.now()
                )
        );
    }

    public void markOutForDelivery(String orderId, String restaurantOwnerId) {
        Order order = orderRepository.findById(orderId);
        assignDeliveryBoy(orderId);

        order = orderRepository.findById(orderId);

        order.getOrderState().next(order); // READY -> OUT_FOR_DELIVERY

        orderRepository.update(order);

        OrderLogService logService = new OrderLogService();

        logService.addLog(
                new OrderLog(
                        orderId,
                        OrderLogAction.DELIVERY_BOY_ASSIGNED,
                        restaurantOwnerId,
                        LocalDateTime.now()
                )
        );

        logService.addLog(
                new OrderLog(
                        orderId,
                        OrderLogAction.ORDER_OUT_FOR_DELIVERY,
                        order.getDeliveryBoyId(),
                        LocalDateTime.now()
                )
        );
    }

    public void deliverOrder(String orderId, String enteredOtp, String deliveryBoyId) {
        Order order = orderRepository.findById(orderId);
        OrderOTP otp = new OrderOTPRepository().findByOrderId(orderId);

        if (otp == null) {
            throw new RuntimeException(MessageConstants.OTP_NOT_FOUND);
        }

        if (!otp.getOtp().equals(enteredOtp)) {
            throw new RuntimeException(MessageConstants.OTP_MISMATCH);
        }

        order.getOrderState().next(order); // OUT_FOR_DELIVERY -> DELIVERED

        orderRepository.update(order);

        OrderLogService logService = new OrderLogService();

        logService.addLog(
                new OrderLog(
                        orderId,
                        OrderLogAction.OTP_VERIFIED,
                        deliveryBoyId,
                        LocalDateTime.now()
                )
        );

        logService.addLog(
                new OrderLog(
                        orderId,
                        OrderLogAction.ORDER_DELIVERED,
                        deliveryBoyId,
                        LocalDateTime.now()
                )
        );
    }

    public void cancelOrder(String orderId, String customerId) {
        Order order = orderRepository.findById(orderId);
        String status = order.getOrderState().getStatus();

        if (status.equals("READY") || status.equals("OUT_FOR_DELIVERY") || status.equals("DELIVERED")) {
            throw new RuntimeException(MessageConstants.ORDER_CANNOT_CANCEL);
        }

        order.setOrderState(new CancelledState());

        orderRepository.update(order);

        new OrderLogService().addLog(
                new OrderLog(
                        orderId,
                        OrderLogAction.ORDER_CANCELLED,
                        customerId,
                        LocalDateTime.now()
                )
        );
    }

    public List<Order> getOrdersByCustomer(String customerId) {

        List<Order> orders = orderRepository.findAll();

        List<Order> result = new ArrayList<>();

        for (Order order : orders) {

            if (order.getCustomerId().equals(customerId))
                result.add(order);
        }

        return result;
    }

}