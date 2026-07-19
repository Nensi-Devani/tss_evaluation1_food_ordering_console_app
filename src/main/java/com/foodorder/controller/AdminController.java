package com.foodorder.controller;

import java.util.List;
import java.util.Scanner;

import com.foodorder.enums.*;
import com.foodorder.model.*;
import com.foodorder.repository.*;
import com.foodorder.service.*;

public class AdminController {
    private final Scanner sc = new Scanner(System.in);

    private final UserService userService = new UserService();
    private final RestaurantService restaurantService = new RestaurantService();
    private final MenuItemService menuItemService = new MenuItemService();
    private final DiscountService discountService = new DiscountService();
    private final OrderService orderService = new OrderService();

    public void menu(User admin) {
        while (true) {
            System.out.println("\n==============================");
            System.out.println("        ADMIN DASHBOARD");
            System.out.println("==============================");
            System.out.println("1. Manage Users");
            System.out.println("2. Manage Restaurants");
            System.out.println("3. Manage Discounts");
            System.out.println("4. View Orders");
            System.out.println("5. Trace Order");
            System.out.println("0. Logout");
            System.out.print("Choice : ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    manageUsers();
                    break;

                case 2:
                    manageRestaurants();
                    break;

                case 3:
                    manageDiscounts();
                    break;

                case 4:
                    viewOrders();
                    break;

                case 5:
                    traceOrder();
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }

    private void manageUsers() {
        while (true) {
            System.out.println("\n------ USER MANAGEMENT ------");
            System.out.println("1. Add User");
            System.out.println("2. View Users");
            System.out.println("3. Activate User");
            System.out.println("4. Deactivate User");
            System.out.println("5. Update User");
            System.out.println("0. Back");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    addUser();
                    break;

                case 2:
                    viewUsers();
                    break;

                case 3:
                    activateUser();
                    break;

                case 4:
                    deactivateUser();
                    break;

                case 5:
                    updateUser();
                    break;

                case 0:
                    return;
            }
        }
    }

    private void addUser() {
        System.out.print("Name : ");
        String name = sc.nextLine();

        System.out.print("Email : ");
        String email = sc.nextLine();

        System.out.print("Password : ");
        String password = sc.nextLine();

        System.out.println("Role");
        System.out.println("1.Customer");
        System.out.println("2.Restaurant");
        System.out.println("3.Delivery Boy");

        int roleChoice = Integer.parseInt(sc.nextLine());

        Role role = Role.CUSTOMER;

        if (roleChoice == 2)
            role = Role.RESTAURANT;

        if (roleChoice == 3)
            role = Role.DELIVERY_BOY;

        User user = new User(
                name,
                email,
                password,
                role,
                Status.ACTIVE
        );

        userService.register(user);

        System.out.println("User Added Successfully.");
    }

    private void viewUsers() {
        List<User> users = new UserRepository().findAll();

        System.out.println();

        System.out.println("\n==============================================================================================================");
        System.out.printf("%-5s %-10s %-25s %-30s %-18s %-12s%n",
                "No",
                "ID",
                "Name",
                "Email",
                "Role",
                "Status");
        System.out.println("==============================================================================================================");

        int no = 1;

        for (User user : users) {
            System.out.printf("%-5d %-10s %-25s %-30s %-18s %-12s%n",
                    no++,
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole(),
                    user.getStatus());
        }

        System.out.println("==============================================================================================================");
    }

    private void activateUser() {
        viewUsers();
        System.out.print("Enter User Id : ");
        String id = sc.nextLine();

        User user = new UserRepository().findById(id);

        if(user.getStatus() == Status.INACTIVE) {
            user.setStatus(Status.ACTIVE);
            new UserRepository().update(user);
            System.out.println("User Activated.");
        }
        else
            System.out.println("User is already Activated.");
    }

    private void deactivateUser() {
        viewUsers();
        System.out.print("Enter User Id : ");
        String id = sc.nextLine();

        User user = new UserRepository().findById(id);

        if(user.getStatus() == Status.ACTIVE) {
            user.setStatus(Status.INACTIVE);
            new UserRepository().update(user);
            System.out.println("User Deactivated.");
        }
        else
            System.out.println("User already Deactivated");
    }

    private void updateUser() {
        viewUsers();
        System.out.print("Enter User Id : ");
        String id = sc.nextLine();

        User user = new UserRepository().findById(id);

        System.out.print("New Name : ");
        user.setName(sc.nextLine());

        System.out.print("New Email : ");
        user.setEmail(sc.nextLine());

        new UserRepository().update(user);

        System.out.println("User Updated.");
    }

    private void manageRestaurants() {
        while (true) {
            System.out.println("\n------ RESTAURANT MANAGEMENT ------");
            System.out.println("1. View Restaurants");
            System.out.println("2. Approve Restaurant");
            System.out.println("3. Activate Restaurant");
            System.out.println("4. Deactivate Restaurant");
            System.out.println("5. View Menu Items");
            System.out.println("0. Back");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    viewRestaurants();
                    break;

                case 2:
                    approveRestaurant();
                    break;

                case 3:
                    activateRestaurant();
                    break;

                case 4:
                    deactivateRestaurant();
                    break;

                case 5:
                    manageMenuItems();
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }

    private void viewRestaurants() {
        List<Restaurant> restaurants = new RestaurantRepository().findAll();

        System.out.println();

        System.out.println("\n==============================================================================================================================");
        System.out.printf("%-5s %-10s %-25s %-12s %-15s %-18s %-12s%n",
                "No",
                "ID",
                "Restaurant",
                "Owner ID",
                "Mobile",
                "City",
                "Status");
        System.out.println("==============================================================================================================================");

        int no = 1;

        for (Restaurant restaurant : restaurants) {
            System.out.printf("%-5d %-10s %-25s %-12s %-15s %-18s %-12s%n",
                    no++,
                    restaurant.getId(),
                    restaurant.getName(),
                    restaurant.getOwnerId(),
                    restaurant.getMobileNumber(),
                    restaurant.getCity(),
                    restaurant.getStatus());
        }

        System.out.println("==============================================================================================================================");
    }

    private void approveRestaurant() {
        System.out.print("Restaurant Id : ");
        String id = sc.nextLine();

        restaurantService.approveRestaurant(id);

        System.out.println("Restaurant Approved.");
    }

    private void activateRestaurant() {
        System.out.print("Restaurant Id : ");
        String id = sc.nextLine();

        Restaurant restaurant = new RestaurantRepository().findById(id);

        if(restaurant.getStatus() == Status.INACTIVE) {
            restaurant.setStatus(Status.ACTIVE);
            new RestaurantRepository().update(restaurant);
            System.out.println("Restaurant Activated.");
        }
        else
            System.out.println("Restaurant already Activated.");
    }

    private void deactivateRestaurant() {
        System.out.print("Restaurant Id : ");
        String id = sc.nextLine();

        Restaurant restaurant = new RestaurantRepository().findById(id);

        if(restaurant.getStatus() == Status.ACTIVE) {
            restaurant.setStatus(Status.INACTIVE);
            new RestaurantRepository().update(restaurant);
            System.out.println("Restaurant Deactivated.");
        }
        else
            System.out.println("Restaurant already Deactivated.");
    }

    private void manageMenuItems() {
        System.out.print("Restaurant Id : ");
        String restaurantId = sc.nextLine();

        List<MenuItem> menuItems = new MenuItemRepository().findByRestaurantId(restaurantId);

        System.out.println();

        System.out.println("\n============================================================================================================================");
        System.out.printf("%-5s %-10s %-25s %-12s %-12s %-12s %-18s %-12s%n",
                "No",
                "ID",
                "Name",
                "Price",
                "Discount",
                "Final Price",
                "Food Type",
                "Status");
        System.out.println("============================================================================================================================");

        int no = 1;

        for (MenuItem item : menuItems) {

            double finalPrice = item.getPrice()
                    - (item.getPrice() * item.getDiscount() / 100);

            System.out.printf("%-5d %-10s %-25s ₹%-11.2f %-11.2f%% ₹%-11.2f %-18s %-12s%n",
                    no++,
                    item.getId(),
                    item.getName(),
                    item.getPrice(),
                    item.getDiscount(),
                    finalPrice,
                    item.getFoodType() + " (" + item.getFoodCategory() + ")",
                    item.getStatus());
        }

        System.out.println("============================================================================================================================");
    }

    private void manageDiscounts() {
        while (true) {
            System.out.println("\n------ DISCOUNT MANAGEMENT ------");
            System.out.println("1. Add Discount");
            System.out.println("2. View Discounts");
            System.out.println("3. Activate Discount");
            System.out.println("4. Deactivate Discount");
            System.out.println("0. Back");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    addDiscount();
                    break;

                case 2:
                    viewDiscounts();
                    break;

                case 3:
                    activateDiscount();
                    break;

                case 4:
                    deactivateDiscount();
                    break;

                case 0:
                    return;
            }
        }
    }

    private void addDiscount() {
        System.out.print("Minimum Amount : ");
        double min = Double.parseDouble(sc.nextLine());

        System.out.print("Discount Percentage : ");
        double percentage = Double.parseDouble(sc.nextLine());

        Discount discount = new Discount(min, percentage, Status.ACTIVE);

        discountService.createDiscount(discount);

        System.out.println("Discount Added Successfully.");
    }

    private void viewDiscounts() {
        List<Discount> discounts = discountService.getAllDiscounts();

        System.out.println("\n==========================================================================================");
        System.out.printf("%-5s %-10s %-20s %-15s %-12s%n",
                "No",
                "ID",
                "Minimum Amount",
                "Discount",
                "Status");
        System.out.println("==========================================================================================");

        int no = 1;

        for (Discount discount : discounts) {
            System.out.printf("%-5d %-10s ₹%-19.2f %-14s %-12s%n",
                    no++,
                    discount.getId(),
                    discount.getMinimumAmount(),
                    String.format("%.2f%%", discount.getDiscountPercentage()),
                    discount.getStatus());
        }

        System.out.println("==========================================================================================");
    }

    private void activateDiscount() {
        viewDiscounts();
        System.out.print("Discount Id : ");
        String id = sc.nextLine();

        Discount discount = new DiscountRepository().findById(id);

        if(discount.getStatus() == Status.INACTIVE) {
            discount.setStatus(Status.ACTIVE);
            new DiscountRepository().update(discount);
            System.out.println("Discount Activated.");
        }
        else
            System.out.println("Discount already Activated.");
    }

    private void deactivateDiscount() {
        System.out.print("Discount Id : ");
        String id = sc.nextLine();

        Discount discount = new DiscountRepository().findById(id);

        if(discount.getStatus() == Status.ACTIVE) {
            discount.setStatus(Status.INACTIVE);
            new DiscountRepository().update(discount);
            System.out.println("Discount Deactivated.");
        }
        else
            System.out.println("Discount already Deactivated.");
    }

    private void viewOrders() {
        List<Order> orders = new OrderRepository().findAll();

        if (orders.isEmpty()) {
            System.out.println("\nNo Orders Found.");
            return;
        }

        System.out.println("\n==========================================================================================================================================================================");
        System.out.printf("%-5s %-10s %-12s %-12s %-15s %-12s %-12s %-12s %-12s %-20s %-22s%n",
                "No",
                "Order ID",
                "Customer",
                "Restaurant",
                "Delivery Boy",
                "Subtotal",
                "Discount",
                "Delivery",
                "Total",
                "Payment",
                "Status",
                "Ordered On");
        System.out.println("==========================================================================================================================================================================");

        int no = 1;

        for (Order order : orders) {

            double discountAmount = order.getSubtotal() * order.getDiscount() / 100;
            double total = order.getSubtotal() - discountAmount + order.getDeliveryCharge();

            System.out.printf("%-5d %-10s %-12s %-12s %-15s ₹%-11.2f %-11.2f%% ₹%-11.2f ₹%-11.2f %-20s %-22s%n",
                    no++,
                    order.getId(),
                    order.getCustomerId(),
                    order.getRestaurantId(),
                    order.getDeliveryBoyId() == null ? "Not Assigned" : order.getDeliveryBoyId(),
                    order.getSubtotal(),
                    order.getDiscount(),
                    order.getDeliveryCharge(),
                    total,
                    order.getPaymentType(),
                    order.getOrderState().getStatus(),
                    order.getOrderDateTime()
            );
        }

        System.out.println("==========================================================================================================================================================================");
    }

    private void traceOrder() {
        viewOrders();
        System.out.print("\nEnter Order Id : ");
        String orderId = sc.nextLine();

        List<OrderLog> logs = new OrderLogRepository().findByOrderId(orderId);

        if (logs.isEmpty()) {
            System.out.println("No Logs Found.");
            return;
        }

        System.out.println("\n========================================================================================================");
        System.out.printf("%-5s %-30s %-20s %-25s%n",
                "No",
                "Action",
                "Performed By",
                "Date & Time");
        System.out.println("========================================================================================================");

        int no = 1;

        for (OrderLog log : logs) {

            System.out.printf("%-5d %-30s %-20s %-25s%n",
                    no++,
                    log.getAction(),
                    log.getActionBy(),
                    log.getActionDateTime());
        }

        System.out.println("========================================================================================================");
    }

}