package com.foodorder.controller;

import java.util.List;
import java.util.Scanner;

import com.foodorder.enums.PaymentType;
import com.foodorder.enums.Status;
import com.foodorder.factory.PaymentFactory;
import com.foodorder.model.*;
import com.foodorder.repository.CartItemRepository;
import com.foodorder.repository.CartRepository;
import com.foodorder.repository.MenuItemRepository;
import com.foodorder.repository.OrderLogRepository;
import com.foodorder.repository.OrderRepository;
import com.foodorder.repository.RestaurantRepository;
import com.foodorder.service.AuthenticationService;
import com.foodorder.service.CartService;
import com.foodorder.service.MenuItemService;
import com.foodorder.service.OrderLogService;
import com.foodorder.service.OrderService;
import com.foodorder.service.PaymentService;
import com.foodorder.service.RestaurantService;

public class CustomerController {

    private final Scanner sc = new Scanner(System.in);

    // Services
    private final AuthenticationService authenticationService = new AuthenticationService();
    private final RestaurantService restaurantService = new RestaurantService();
    private final MenuItemService menuItemService = new MenuItemService();
    private final CartService cartService = new CartService();
    private final OrderService orderService = new OrderService();
    private final PaymentService paymentService = new PaymentService();
    private final OrderLogService orderLogService = new OrderLogService();

    // Repositories
    private final RestaurantRepository restaurantRepository = new RestaurantRepository();
    private final MenuItemRepository menuItemRepository = new MenuItemRepository();
    private final CartRepository cartRepository = new CartRepository();
    private final CartItemRepository cartItemRepository = new CartItemRepository();
    private final OrderRepository orderRepository = new OrderRepository();
    private final OrderLogRepository orderLogRepository = new OrderLogRepository();

    public void menu(User customer) {

        while (true) {

            System.out.println("\n==================================");
            System.out.println("        CUSTOMER DASHBOARD");
            System.out.println("==================================");
            System.out.println("1. View Restaurants");
            System.out.println("2. View Restaurant Menu");
            System.out.println("3. Cart");
            System.out.println("4. My Orders");
            System.out.println("0. Logout");
            System.out.print("Enter Choice : ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:
                    viewRestaurants();
                    break;

                case 2:
                    viewRestaurantMenu(customer);
                    break;

                case 3:
                    cartMenu(customer);
                    break;

                case 4:
                    myOrders(customer);
                    break;

                case 0:
                    logout();
                    return;

                default:
                    System.out.println("Invalid Choice.");
            }
        }
    }

    private void viewRestaurants() {

        List<Restaurant> restaurants = restaurantRepository.findAllActive();

        if (restaurants.isEmpty()) {
            System.out.println("No Restaurants Available.");
            return;
        }

        System.out.println("\n========== RESTAURANTS ==========");

        for (Restaurant restaurant : restaurants) {

            System.out.println("----------------------------------");
            System.out.println("Restaurant Id : " + restaurant.getId());
            System.out.println("Name          : " + restaurant.getName());
            System.out.println("City          : " + restaurant.getCity());
            System.out.println("Mobile        : " + restaurant.getMobileNumber());
        }
    }

    private void viewRestaurantMenu(User customer) {

        System.out.print("Enter Restaurant Id : ");
        String restaurantId = sc.nextLine();

        List<MenuItem> menuItems =
                menuItemRepository.findByRestaurantId(restaurantId);

        if (menuItems.isEmpty()) {
            System.out.println("No Menu Items Found.");
            return;
        }

        System.out.println("\n============== MENU ==============");

        for (MenuItem item : menuItems) {

            if (item.getStatus() != Status.ACTIVE)
                continue;

            System.out.println("----------------------------------");
            System.out.println("Id         : " + item.getId());
            System.out.println("Name       : " + item.getName());
            System.out.println("Price      : ₹" + item.getPrice());
            System.out.println("Discount   : " + item.getDiscount() + "%");
            System.out.println("Food Type  : " + item.getFoodType());
            System.out.println("Category   : " + item.getFoodCategory());
        }

        System.out.println("\n1. Add To Cart");
        System.out.println("0. Back");

        int choice = Integer.parseInt(sc.nextLine());

        if (choice == 1) {
            addToCart(customer, restaurantId);
        }
    }

    private void addToCart(User customer, String restaurantId) {

        Cart cart = cartRepository.findByCustomerId(customer.getId());

        if (cart == null) {

            cart = new Cart(customer.getId(), restaurantId);

            cartService.createCart(cart);

            cart = cartRepository.findByCustomerId(customer.getId());
        } else if (!cart.getRestaurantId().equals(restaurantId)) {

            System.out.println("You already have items from another restaurant.");
            System.out.println("Please place that order first.");

            return;
        }

        System.out.print("Enter Menu Item Id : ");
        String menuItemId = sc.nextLine();

        System.out.print("Enter Quantity : ");
        int quantity = Integer.parseInt(sc.nextLine());

        CartItem cartItem = new CartItem(
                cart.getId(),
                menuItemId,
                quantity
        );

        cartService.addItem(cartItem);

        System.out.println("Item Added To Cart Successfully.");
    }

    private void cartMenu(User customer) {

        while (true) {

            System.out.println("\n========== CART ==========");
            System.out.println("1. View Cart");
            System.out.println("2. Remove Item");
            System.out.println("3. Place Order");
            System.out.println("0. Back");
            System.out.print("Enter Choice : ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:
                    viewCart(customer);
                    break;

                case 2:
                    removeCartItem(customer);
                    break;

                case 3:
                    checkout(customer);
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid Choice.");
            }
        }
    }

    private void viewCart(User customer) {

        try {

            Cart cart = cartService.getCart(customer.getId());

            List<CartItem> cartItems = cartService.getCartItems(cart.getId());

            if (cartItems.isEmpty()) {

                System.out.println("Cart is Empty.");
                return;
            }

            double total = 0;

            System.out.println("\n========== CART ==========");

            for (CartItem cartItem : cartItems) {

                MenuItem menuItem =
                        menuItemRepository.findById(cartItem.getMenuItemId());

                double price =
                        menuItem.getPrice()
                                - (menuItem.getPrice() * menuItem.getDiscount() / 100);

                double amount = price * cartItem.getQuantity();

                total += amount;

                System.out.println("--------------------------------");
                System.out.println("Cart Item Id : " + cartItem.getId());
                System.out.println("Food         : " + menuItem.getName());
                System.out.println("Price        : " + price);
                System.out.println("Quantity     : " + cartItem.getQuantity());
                System.out.println("Amount       : " + amount);
            }

            System.out.println("--------------------------------");
            System.out.println("Cart Total : " + total);

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    private void removeCartItem(User customer) {

        try {

            viewCart(customer);

            System.out.print("\nEnter Cart Item Id : ");

            String cartItemId = sc.nextLine();

            cartService.removeItem(cartItemId);

            System.out.println("Item Removed Successfully.");

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    private void checkout(User customer) {

        try {

            Cart cart = cartService.getCart(customer.getId());

            List<CartItem> cartItems = cartService.getCartItems(cart.getId());

            if (cartItems.isEmpty()) {

                System.out.println("Cart is Empty.");
                return;
            }

            System.out.println("\n========== PAYMENT ==========");
            System.out.println("1. Cash On Delivery");
            System.out.println("2. UPI");
            System.out.print("Enter Choice : ");

            int choice = Integer.parseInt(sc.nextLine());

            PaymentType paymentType;

            switch (choice) {

                case 1:
                    paymentType = PaymentType.CASH;
                    break;

                case 2:
                    paymentType = PaymentType.UPI;
                    break;

                default:
                    System.out.println("Invalid Payment Type.");
                    return;
            }

            Order order = orderService.placeOrder(
                    customer.getId(),
                    paymentType,
                    40
            );

            Payment payment =
                    paymentService.getPaymentByOrderId(order.getId());

            PaymentFactory
                    .getPaymentStrategy(paymentType)
                    .pay(payment);

            paymentService.updatePaymentStatus(
                    payment.getId(),
                    payment.getPaymentStatus()
            );

            System.out.println("\n==================================");
            System.out.println("Order Placed Successfully");
            System.out.println("==================================");
            System.out.println("Order Id : " + order.getId());

            System.out.println(
                    "Total Amount : ₹" +
                            (order.getSubtotal()
                                    - order.getDiscount()
                                    + order.getDeliveryCharge())
            );

            System.out.println("Payment : "
                    + payment.getPaymentStatus());

            if (paymentType == PaymentType.CASH) {

                System.out.println("Cash will be collected at delivery.");

            } else {

                System.out.println("UPI Payment Successful.");
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    /*------------------------------------------------------*/

    private void myOrders(User customer) {

        while (true) {

            System.out.println("\n------ MY ORDERS ------");
            System.out.println("1. View Orders");
            System.out.println("2. Cancel Order");
            System.out.println("3. Track Order");
            System.out.println("0. Back");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:
                    viewOrders(customer);
                    break;

                case 2:
                    cancelOrder(customer);
                    break;

                case 3:
                    trackOrder(customer);
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }

    /*------------------------------------------------------*/

    private void viewOrders(User customer) {

        List<Order> orders =
                new OrderRepository().findByCustomerId(customer.getId());

        if (orders.isEmpty()) {

            System.out.println("No Orders Found.");
            return;
        }

        for (Order order : orders) {

            System.out.println("--------------------------------");
            System.out.println("Order Id : " + order.getId());
            System.out.println("Restaurant : " + order.getRestaurantId());
            System.out.println("Subtotal : " + order.getSubtotal());
            System.out.println("Discount : " + order.getDiscount());
            System.out.println("Delivery Charge : " + order.getDeliveryCharge());

            System.out.println("Total : " +
                    (order.getSubtotal()
                            - order.getDiscount()
                            + order.getDeliveryCharge()));

            System.out.println("Payment : " + order.getPaymentType());

            System.out.println("Status : " +
                    order.getOrderState().getStatus());
        }
    }

    /*------------------------------------------------------*/

    private void cancelOrder(User customer) {

        viewOrders(customer);

        System.out.print("\nEnter Order Id : ");

        String orderId = sc.nextLine();

        try {

            orderService.cancelOrder(
                    orderId,
                    customer.getId()
            );

            System.out.println("Order Cancelled Successfully.");

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

    }

    /*------------------------------------------------------*/

    private void trackOrder(User customer) {

        viewOrders(customer);

        System.out.print("\nEnter Order Id : ");

        String orderId = sc.nextLine();

        List<OrderLog> logs =
                new OrderLogRepository().findByOrderId(orderId);

        if (logs.isEmpty()) {

            System.out.println("No Tracking Available.");
            return;
        }

        System.out.println();

        for (OrderLog log : logs) {

            System.out.println("--------------------------------");
            System.out.println(log.getActionDateTime());
            System.out.println(log.getAction());
            System.out.println("By : " + log.getActionBy());

        }

    }

    private void logout() {

        authenticationService.logout();

        System.out.println("Logged Out Successfully.");

    }

}

/*------------------------------------------------------*/