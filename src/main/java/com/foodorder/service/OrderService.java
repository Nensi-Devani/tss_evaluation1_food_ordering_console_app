package com.foodorder.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.enums.PaymentType;
import com.foodorder.exception.CartNotFoundException;
import com.foodorder.exception.DeliveryBoyNotAvailableException;
import com.foodorder.exception.OrderNotFoundException;
import com.foodorder.model.*;
import com.foodorder.repository.*;
import com.foodorder.state.OrderState;
import com.foodorder.state.PlacedState;
import com.foodorder.util.FileUtil;

public class OrderService {
    private final OrderRepository orderRepository = new OrderRepository();
    private final OrderItemRepository orderItemRepository = new OrderItemRepository();
    private final CartRepository cartRepository = new CartRepository();
    private final CartItemRepository cartItemRepository = new CartItemRepository();
    private final MenuItemRepository menuItemRepository = new MenuItemRepository();

    public Order placeOrder(String customerId, PaymentType paymentType, double deliveryCharge) {
        Cart cart = cartRepository.findByCustomerId(customerId);

        if (cart == null) {
            throw new CartNotFoundException(MessageConstants.CART_NOT_FOUND);
        }

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        double subtotal = 0;

        for (CartItem cartItem : cartItems) {
            MenuItem menuItem = menuItemRepository.findById(cartItem.getMenuItemId());

            double priceAfterDiscount = menuItem.getPrice() - (menuItem.getPrice() * menuItem.getDiscount() / 100);

            subtotal += priceAfterDiscount * cartItem.getQuantity();
        }

        Order order = new Order(
                customerId,
                cart.getRestaurantId(),
                null,
                LocalDateTime.now(),
                subtotal,
                0,
                deliveryCharge,
                paymentType,
                null // state set below
        );

        order.setOrderState(new PlacedState());

        orderRepository.save(order);

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

        List<CartItem> allCartItems = cartItemRepository.findAll();
        allCartItems.removeIf(item -> item.getCartId().equals(cart.getId()));

        FileUtil.writeData(FileConstants.CART_ITEMS_FILE, allCartItems);

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

//        if (!order.getOrderState().toString().equals("OUT_FOR_DELIVERY")) {
//            throw new RuntimeException("Order is not ready for delivery");
//        }

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
}