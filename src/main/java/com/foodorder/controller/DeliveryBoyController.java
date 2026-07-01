package com.foodorder.controller;

import java.util.List;
import java.util.Scanner;

import com.foodorder.model.Order;
import com.foodorder.model.User;
import com.foodorder.repository.OrderRepository;
import com.foodorder.service.AuthenticationService;
import com.foodorder.service.OrderService;

public class DeliveryBoyController {

    private final Scanner sc = new Scanner(System.in);

    private final OrderRepository orderRepository =
            new OrderRepository();

    private final OrderService orderService =
            new OrderService();

    private final AuthenticationService authService =
            new AuthenticationService();

    public void menu(User deliveryBoy) {

        while (true) {

            System.out.println("\n==============================");
            System.out.println(" DELIVERY BOY DASHBOARD");
            System.out.println("==============================");
            System.out.println("1. Dashboard");
            System.out.println("2. Assigned Orders");
            System.out.println("3. Today's Deliveries");
            System.out.println("0. Logout");

            System.out.print("Choice : ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:
                    dashboard(deliveryBoy);
                    break;

                case 2:
                    assignedOrders(deliveryBoy);
                    break;

                case 3:
                    todayDeliveries(deliveryBoy);
                    break;

                case 0:
                    logout();
                    return;

                default:
                    System.out.println("Invalid Choice.");
            }
        }
    }

    /*------------------------------------------------------*/

    private void dashboard(User deliveryBoy) {

        List<Order> orders =
                orderRepository.findByDeliveryBoyId(deliveryBoy.getId());

        int assigned = 0;
        int delivered = 0;

        for (Order order : orders) {

            if (order.getOrderState().getStatus().equals("DELIVERED")) {
                delivered++;
            } else {
                assigned++;
            }
        }

        System.out.println("\n========== DASHBOARD ==========");
        System.out.println("Delivery Boy : " + deliveryBoy.getName());
        System.out.println("Assigned Orders : " + assigned);
        System.out.println("Delivered Orders : " + delivered);
    }

    /*------------------------------------------------------*/

    private void assignedOrders(User deliveryBoy) {

        List<Order> orders =
                orderRepository.findByDeliveryBoyId(deliveryBoy.getId());

        if (orders.isEmpty()) {

            System.out.println("No Assigned Orders.");
            return;
        }

        System.out.println("\n========== ASSIGNED ORDERS ==========");

        boolean found = false;

        for (Order order : orders) {

            if (order.getOrderState().getStatus().equals("DELIVERED"))
                continue;

            found = true;

            System.out.println("--------------------------------");
            System.out.println("Order Id       : " + order.getId());
            System.out.println("Customer Id    : " + order.getCustomerId());
            System.out.println("Restaurant Id  : " + order.getRestaurantId());
            System.out.println("Amount         : "
                    + (order.getSubtotal()
                    - order.getDiscount()
                    + order.getDeliveryCharge()));
            System.out.println("Status         : "
                    + order.getOrderState().getStatus());
        }

        if (!found) {

            System.out.println("No Pending Orders.");
            return;
        }

        System.out.println();
        System.out.println("1. Deliver Order");
        System.out.println("0. Back");

        int choice = Integer.parseInt(sc.nextLine());

        if (choice == 1) {
            verifyOTP(deliveryBoy);
        }
    }

/*------------------------------------------------------*/
    /*------------------------------------------------------*/

    private void verifyOTP(User deliveryBoy) {

        try {

            System.out.print("Enter Order Id : ");
            String orderId = sc.nextLine();

            System.out.print("Enter OTP : ");
            String otp = sc.nextLine();

            orderService.deliverOrder(
                    orderId,
                    otp,
                    deliveryBoy.getId()
            );

            System.out.println("\nOrder Delivered Successfully.");

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    /*------------------------------------------------------*/

    private void todayDeliveries(User deliveryBoy) {

        List<Order> orders =
                orderRepository.findByDeliveryBoyId(deliveryBoy.getId());

        int delivered = 0;

        System.out.println("\n========== TODAY'S DELIVERIES ==========");

        for (Order order : orders) {

            if (order.getOrderState().getStatus().equals("DELIVERED")) {

                delivered++;

                System.out.println("--------------------------------");
                System.out.println("Order Id : " + order.getId());
                System.out.println("Customer : " + order.getCustomerId());
                System.out.println("Restaurant : " + order.getRestaurantId());

                System.out.println("Amount : " +
                        (order.getSubtotal()
                                - order.getDiscount()
                                + order.getDeliveryCharge()));
            }
        }

        if (delivered == 0) {

            System.out.println("No Deliveries Today.");
        }
    }

/*------------------------------------------------------*/
    /*------------------------------------------------------*/

    private void logout() {

        authService.logout();

        new LoginController().start();
    }
}