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

    private final OrderRepository orderRepository = new OrderRepository();
    private final OrderService orderService = new OrderService();
    private final AuthenticationService authService = new AuthenticationService();

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

                default: System.out.println("Invalid Choice.");
            }
        }
    }

    private void dashboard(User deliveryBoy) {
        List<Order> orders = orderRepository.findByDeliveryBoyId(deliveryBoy.getId());

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

    private void assignedOrders(User deliveryBoy) {
        List<Order> orders = orderRepository.findByDeliveryBoyId(deliveryBoy.getId());

        if (orders.isEmpty()) {
            System.out.println("No Assigned Orders.");
            return;
        }

        System.out.println("\n==============================================================================================================");
        System.out.printf("%-5s %-12s %-15s %-15s %-12s %-20s%n",
                "No",
                "Order ID",
                "Customer",
                "Restaurant",
                "Amount",
                "Status");
        System.out.println("==============================================================================================================");

        boolean found = false;
        int no = 1;

        for (Order order : orders) {
            if (order.getOrderState().getStatus().equals("DELIVERED"))
                continue;

            found = true;

            double total = order.getSubtotal() - order.getDiscount() + order.getDeliveryCharge();

            System.out.printf("%-5d %-12s %-15s %-15s ₹%-11.2f %-20s%n",
                    no++,
                    order.getId(),
                    order.getCustomerId(),
                    order.getRestaurantId(),
                    total,
                    order.getOrderState().getStatus());
        }

        if (!found) {
            System.out.println("No Assigned Orders Found.");
            return;
        }

        System.out.println("==============================================================================================================");

        System.out.println();
        System.out.println("1. Deliver Order");
        System.out.println("0. Back");

        int choice = Integer.parseInt(sc.nextLine());

        if (choice == 1) {
            verifyOTP(deliveryBoy);
        }
    }

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

    private void todayDeliveries(User deliveryBoy) {
        List<Order> orders = orderRepository.findByDeliveryBoyId(deliveryBoy.getId());

        int delivered = 0;

        System.out.println("\n==============================================================================================================");
        System.out.printf("%-5s %-12s %-15s %-15s %-12s%n",
                "No",
                "Order ID",
                "Customer",
                "Restaurant",
                "Amount");
        System.out.println("==============================================================================================================");

        int no = 1;

        for (Order order : orders) {
            if (!order.getOrderState().getStatus().equals("DELIVERED"))
                continue;

            delivered++;

            double total = order.getSubtotal() - order.getDiscount() + order.getDeliveryCharge();

            System.out.printf("%-5d %-12s %-15s %-15s ₹%-11.2f%n",
                    no++,
                    order.getId(),
                    order.getCustomerId(),
                    order.getRestaurantId(),
                    total);
        }

        if (delivered == 0) {
            System.out.println("No Deliveries Found.");
        }

        System.out.println("==============================================================================================================");
        System.out.println("Total Deliveries : " + delivered);

        if (delivered == 0) {
            System.out.println("No Deliveries Today.");
        }
    }

    private void logout() {
        authService.logout();
    }
}