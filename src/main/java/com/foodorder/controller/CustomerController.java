package com.foodorder.controller;

import java.util.List;
import java.util.Scanner;

import com.foodorder.constants.AppConstants;
import com.foodorder.enums.PaymentType;
import com.foodorder.enums.Status;
import com.foodorder.factory.PaymentFactory;
import com.foodorder.model.*;
import com.foodorder.repository.*;
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
//    private final RestaurantService restaurantService = new RestaurantService();
//    private final MenuItemService menuItemService = new MenuItemService();
    private final CartService cartService = new CartService();
    private final OrderService orderService = new OrderService();
    private final PaymentService paymentService = new PaymentService();
    private final OrderLogService orderLogService = new OrderLogService();

    // Repositories
    private final RestaurantRepository restaurantRepository = new RestaurantRepository();
    private final MenuItemRepository menuItemRepository = new MenuItemRepository();
    private final CartRepository cartRepository = new CartRepository();
//    private final CartItemRepository cartItemRepository = new CartItemRepository();
//    private final OrderRepository orderRepository = new OrderRepository();
//    private final OrderLogRepository orderLogRepository = new OrderLogRepository();

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

        System.out.println("\n======================================================================================================");
        System.out.printf("%-5s %-12s %-30s %-20s %-15s%n", "No", "ID", "Restaurant Name", "City", "Mobile");
        System.out.println("======================================================================================================");

        int no = 1;

        for (Restaurant restaurant : restaurants) {
            if (restaurant.getStatus() != Status.ACTIVE)
                continue;
            System.out.printf("%-5d %-12s %-30s %-20s %-15s%n",
                    no++,
                    restaurant.getId(),
                    restaurant.getName(),
                    restaurant.getCity(),
                    restaurant.getMobileNumber());
        }

        System.out.println("======================================================================================================");
    }

    private void viewRestaurantMenu(User customer) {
        System.out.print("Enter Restaurant Id : ");
        String restaurantId = sc.nextLine();

        List<MenuItem> menuItems = menuItemRepository.findByRestaurantId(restaurantId);

        if (menuItems.isEmpty()) {
            System.out.println("No Menu Items Found.");
            return;
        }

        System.out.println("\n==============================================================================================================================");
        System.out.printf("%-5s %-10s %-25s %-10s %-10s %-12s %-18s%n",
                "No",
                "ID",
                "Name",
                "Price",
                "Discount",
                "Type",
                "Category");
        System.out.println("==============================================================================================================================");

        int no = 1;

        for (MenuItem item : menuItems) {
            if (item.getStatus() != Status.ACTIVE)
                continue;

            System.out.printf("%-5d %-10s %-25s ₹%-10.2f %-12s %-12s %-18s%n",
                    no++,
                    item.getId(),
                    item.getName(),
                    item.getPrice(),
                    item.getDiscount() + "%",
                    item.getFoodType(),
                    item.getFoodCategory());
        }

        System.out.println("==============================================================================================================================");

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
        }
        else if (!cart.getRestaurantId().equals(restaurantId)) {
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

            System.out.println("\n========================================================================================================================================");
            System.out.printf("%-5s %-10s %-25s %-12s %-10s %-12s %-12s %-12s%n",
                    "No",
                    "Cart ID",
                    "Food Name",
                    "Price",
                    "Qty",
                    "Discount",
                    "Amount",
                    "Status");
            System.out.println("========================================================================================================================================");

            int no = 1;

            for (CartItem cartItem : cartItems) {
                MenuItem menuItem = menuItemRepository.findById(cartItem.getMenuItemId());

                double actualPrice = menuItem.getPrice();
                double discount = menuItem.getDiscount();

                double priceAfterDiscount =
                        actualPrice - (actualPrice * discount / 100);

                double amount =
                        priceAfterDiscount * cartItem.getQuantity();

                total += amount;

                System.out.printf("%-5d %-10s %-25s ₹%-11.2f %-10d %-12s ₹%-11.2f %-12s%n",
                        no++,
                        cartItem.getId(),
                        menuItem.getName(),
                        priceAfterDiscount,
                        cartItem.getQuantity(),
                        String.format("%.2f%%", discount),
                        amount,
                        menuItem.getStatus());
            }

            System.out.println("========================================================================================================================================");
            System.out.printf("%97s ₹%.2f%n", "Grand Total :", total);
            System.out.println("========================================================================================================================================");

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

            UserAddressRepository addressRepository = new UserAddressRepository();
            List<UserAddress> addresses = addressRepository.findByUserId(customer.getId());

            UserAddress selectedAddress = null;
            if (addresses.isEmpty()) {
                System.out.println("\nNo delivery address found.");
                System.out.println("Please add a delivery address first.\n");

                System.out.print("Mobile Number : ");
                String mobile = sc.nextLine();

                System.out.print("Address : ");
                String address = sc.nextLine();

                System.out.print("City : ");
                String city = sc.nextLine();

                System.out.print("Pincode : ");
                String pincode = sc.nextLine();

                UserAddress userAddress = new UserAddress(
                        customer.getId(),
                        mobile,
                        address,
                        city,
                        pincode
                );

                addressRepository.save(userAddress);
                addresses = addressRepository.findByUserId(customer.getId());
            } else {

                System.out.println("\n===============================================================");
                System.out.printf("%-5s %-15s %-30s %-15s %-10s%n",
                        "No",
                        "Mobile",
                        "Address",
                        "City",
                        "Pincode");
                System.out.println("===============================================================");

                for (int i = 0; i < addresses.size(); i++) {

                    UserAddress address = addresses.get(i);

                    System.out.printf("%-5d %-15s %-30s %-15s %-10s%n",
                            (i + 1),
                            address.getMobileNumber(),
                            address.getAddress(),
                            address.getCity(),
                            address.getPincode());
                }

                System.out.println("===============================================================");
                System.out.println((addresses.size() + 1) + ". Add New Address");

                System.out.print("Select Address : ");
                int addressChoice = Integer.parseInt(sc.nextLine());

                if (addressChoice == addresses.size() + 1) {

                    System.out.print("Mobile Number : ");
                    String mobile = sc.nextLine();

                    System.out.print("Address : ");
                    String address = sc.nextLine();

                    System.out.print("City : ");
                    String city = sc.nextLine();

                    System.out.print("Pincode : ");
                    String pincode = sc.nextLine();

                    UserAddress userAddress = new UserAddress(
                            customer.getId(),
                            mobile,
                            address,
                            city,
                            pincode
                    );

                    addressRepository.save(userAddress);
                    addresses = addressRepository.findByUserId(customer.getId());

                    selectedAddress = addresses.get(addresses.size() - 1);

                } else {

                    if (addressChoice < 1 || addressChoice > addresses.size()) {
                        System.out.println("Invalid Address Selection.");
                        return;
                    }

                    selectedAddress = addresses.get(addressChoice - 1);
                }
            }

// If there were no addresses initially, use the newly added one.
            if (addresses.size() == 1 && selectedAddress == null) {
                selectedAddress = addresses.get(0);
            }

//            if (addresses.isEmpty()) {
//                System.out.println("\nNo delivery address found.");
//                System.out.println("Please add a delivery address first.\n");
//
//                System.out.print("Mobile Number : ");
//                String mobile = sc.nextLine();
//
//                System.out.print("Address : ");
//                String address = sc.nextLine();
//
//                System.out.print("City : ");
//                String city = sc.nextLine();
//
//                System.out.print("Pincode : ");
//                String pincode = sc.nextLine();
//
//                UserAddress userAddress = new UserAddress(
//                        customer.getId(),
//                        mobile,
//                        address,
//                        city,
//                        pincode
//                );
//
//                addressRepository.save(userAddress);
//
//                addresses = addressRepository.findByUserId(customer.getId());
//            }
//
//            System.out.println("\n===============================================================");
//            System.out.printf("%-5s %-15s %-30s %-15s %-10s%n",
//                    "No",
//                    "Mobile",
//                    "Address",
//                    "City",
//                    "Pincode");
//            System.out.println("===============================================================");
//
//            for (int i = 0; i < addresses.size(); i++) {
//
//                UserAddress address = addresses.get(i);
//
//                System.out.printf("%-5d %-15s %-30s %-15s %-10s%n",
//                        (i + 1),
//                        address.getMobileNumber(),
//                        address.getAddress(),
//                        address.getCity(),
//                        address.getPincode());
//            }
//
//            System.out.println("===============================================================");
//
//            System.out.print("Select Address : ");
//            int addressChoice = Integer.parseInt(sc.nextLine());
//
//            if (addressChoice < 1 || addressChoice > addresses.size()) {
//                System.out.println("Invalid Address Selection.");
//                return;
//            }
//
//            UserAddress selectedAddress = addresses.get(addressChoice - 1);

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

            int totalItems = cartItems.stream()
                    .mapToInt(CartItem::getQuantity)
                    .sum();

            double deliveryCharge;
            if (totalItems <= 2) {
                deliveryCharge = AppConstants.DELIVERY_CHARGE_UPTO_2_ITEMS;
            } else if (totalItems <= 4) {
                deliveryCharge = AppConstants.DELIVERY_CHARGE_UPTO_4_ITEMS;
            } else {
                deliveryCharge = AppConstants.DELIVERY_CHARGE_ABOVE_4_ITEMS;
            }

            Order order = orderService.placeOrder(
                    customer.getId(),
                    selectedAddress.getId(),
                    paymentType,
                    deliveryCharge
            );

            Payment payment = paymentService.getPaymentByOrderId(order.getId());

            PaymentFactory
                    .getPaymentStrategy(paymentType)
                    .pay(payment);

            paymentService.updatePaymentStatus(
                    payment.getId(),
                    payment.getPaymentStatus()
            );

            OrderOTP otp = new OrderOTPRepository().findByOrderId(order.getId());

            System.out.println("\n======================================");
            System.out.println("      ORDER PLACED SUCCESSFULLY");
            System.out.println("======================================");

            System.out.println("Order Id : " + order.getId());

            System.out.println("\nDelivery Address");
            System.out.println("--------------------------------------");
            System.out.println(selectedAddress.getAddress());
            System.out.println(selectedAddress.getCity());
            System.out.println(selectedAddress.getPincode());

            System.out.println("\nTotal Amount : ₹"
                    + (order.getSubtotal()
                    - order.getDiscount()
                    + order.getDeliveryCharge()));

            System.out.println("Payment Status : " + payment.getPaymentStatus());

            System.out.println("\nDelivery OTP : " + otp.getOtp());

            System.out.println("\nShare this OTP only with the delivery boy during delivery.");

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

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

    private void viewOrders(User customer) {
        List<Order> orders = new OrderRepository().findByCustomerId(customer.getId());

        if (orders.isEmpty()) {
            System.out.println("No Orders Found.");
            return;
        }

        System.out.println("\n==============================================================================================================================");
        System.out.printf("%-5s %-10s %-15s %-12s %-12s %-12s %-12s %-10s %-18s%n",
                "No",
                "Order ID",
                "Restaurant",
                "Subtotal",
                "Discount",
                "Delivery",
                "Total",
                "Payment",
                "Status");
        System.out.println("==============================================================================================================================");

        int no = 1;

        for (Order order : orders) {
            double total = order.getSubtotal() - order.getDiscount() + order.getDeliveryCharge();

            System.out.printf("%-5d %-10s %-15s ₹%-11.2f ₹%-11.2f ₹%-11.2f ₹%-11.2f %-10s %-18s%n",
                    no++,
                    order.getId(),
                    order.getRestaurantId(),
                    order.getSubtotal(),
                    order.getDiscount(),
                    order.getDeliveryCharge(),
                    total,
                    order.getPaymentType(),
                    order.getOrderState().getStatus());
        }

        System.out.println("==============================================================================================================================");
    }

    private void cancelOrder(User customer) {
        viewOrders(customer);

        System.out.print("\nEnter Order Id : ");
        String orderId = sc.nextLine();

        try {
            orderService.cancelOrder(orderId, customer.getId());
            System.out.println("Order Cancelled Successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void trackOrder(User customer) {
        viewOrders(customer);

        System.out.print("\nEnter Order Id : ");
        String orderId = sc.nextLine();

        List<OrderLog> logs = new OrderLogRepository().findByOrderId(orderId);

        if (logs.isEmpty()) {
            System.out.println("No Tracking Available.");
            return;
        }

        System.out.println();

        System.out.println("\n==============================================================================================================");
        System.out.printf("%-5s %-25s %-30s %-20s%n",
                "No",
                "Date & Time",
                "Action",
                "Performed By");
        System.out.println("==============================================================================================================");

        int no = 1;

        for (OrderLog log : logs) {
            System.out.printf("%-5d %-25s %-30s %-20s%n",
                    no++,
                    log.getActionDateTime(),
                    log.getAction(),
                    log.getActionBy());
        }

        System.out.println("==============================================================================================================");
    }

    private void logout() {
        authenticationService.logout();
    }

}
