package com.foodorder.controller;

import java.time.LocalDateTime;
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

    /*----------------------------------------------------*/

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

        for (User user : users) {

            System.out.println("--------------------------------");
            System.out.println("ID      : " + user.getId());
            System.out.println("Name    : " + user.getName());
            System.out.println("Email   : " + user.getEmail());
            System.out.println("Role    : " + user.getRole());
            System.out.println("Status  : " + user.getStatus());

        }

    }

    private void activateUser() {

        System.out.print("Enter User Id : ");

        String id = sc.nextLine();

        User user = new UserRepository().findById(id);

        user.setStatus(Status.ACTIVE);

        new UserRepository().update(user);

        System.out.println("User Activated.");

    }

    private void deactivateUser() {

        System.out.print("Enter User Id : ");

        String id = sc.nextLine();

        User user = new UserRepository().findById(id);

        user.setStatus(Status.INACTIVE);

        new UserRepository().update(user);

        System.out.println("User Deactivated.");

    }

    private void updateUser() {

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

    /*----------------------------------------------------*/
    /*                RESTAURANT MANAGEMENT                */
    /*----------------------------------------------------*/

    private void manageRestaurants() {

        while (true) {

            System.out.println("\n------ RESTAURANT MANAGEMENT ------");
            System.out.println("1. View Restaurants");
            System.out.println("2. Approve Restaurant");
            System.out.println("3. Activate Restaurant");
            System.out.println("4. Deactivate Restaurant");
            System.out.println("5. Manage Menu Items");
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

        for (Restaurant restaurant : restaurants) {

            System.out.println("--------------------------------");
            System.out.println("ID        : " + restaurant.getId());
            System.out.println("Name      : " + restaurant.getName());
            System.out.println("Owner Id  : " + restaurant.getOwnerId());
            System.out.println("Mobile    : " + restaurant.getMobileNumber());
            System.out.println("City      : " + restaurant.getCity());
            System.out.println("Status    : " + restaurant.getStatus());
        }
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

        restaurant.setStatus(Status.ACTIVE);

        new RestaurantRepository().update(restaurant);

        System.out.println("Restaurant Activated.");
    }

    private void deactivateRestaurant() {

        System.out.print("Restaurant Id : ");

        String id = sc.nextLine();

        Restaurant restaurant = new RestaurantRepository().findById(id);

        restaurant.setStatus(Status.INACTIVE);

        new RestaurantRepository().update(restaurant);

        System.out.println("Restaurant Deactivated.");
    }

    /*----------------------------------------------------*/
    /*                MENU ITEM MANAGEMENT                */
    /*----------------------------------------------------*/

    private void manageMenuItems() {

        System.out.print("Restaurant Id : ");

        String restaurantId = sc.nextLine();

        List<MenuItem> menuItems =
                new MenuItemRepository().findByRestaurantId(restaurantId);

        System.out.println();

        for (MenuItem item : menuItems) {

            System.out.println("--------------------------------");
            System.out.println("Id         : " + item.getId());
            System.out.println("Name       : " + item.getName());
            System.out.println("Price      : " + item.getPrice());
            System.out.println("Discount   : " + item.getDiscount());
            System.out.println("Food Type  : " + item.getFoodType());
            System.out.println("Category   : " + item.getFoodCategory());
            System.out.println("Status     : " + item.getStatus());
        }
    }

    /*----------------------------------------------------*/
    /*                DISCOUNT MANAGEMENT                 */
    /*----------------------------------------------------*/

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

        Discount discount = new Discount(
                min,
                percentage,
                Status.ACTIVE
        );

        discountService.createDiscount(discount);

        System.out.println("Discount Added Successfully.");
    }

    private void viewDiscounts() {

        List<Discount> discounts = discountService.getAllDiscounts();

        for (Discount discount : discounts) {

            System.out.println("--------------------------------");
            System.out.println("ID          : " + discount.getId());
            System.out.println("Minimum Amt : " + discount.getMinimumAmount());
            System.out.println("Discount %  : " + discount.getDiscountPercentage());
            System.out.println("Status      : " + discount.getStatus());
        }
    }

    private void activateDiscount() {

        System.out.print("Discount Id : ");

        String id = sc.nextLine();

        Discount discount = new DiscountRepository().findById(id);

        discount.setStatus(Status.ACTIVE);

        new DiscountRepository().update(discount);

        System.out.println("Discount Activated.");
    }

    private void deactivateDiscount() {

        System.out.print("Discount Id : ");

        String id = sc.nextLine();

        Discount discount = new DiscountRepository().findById(id);

        discount.setStatus(Status.INACTIVE);

        new DiscountRepository().update(discount);

        System.out.println("Discount Deactivated.");
    }

    /*----------------------------------------------------*/
    /*                    ORDER MANAGEMENT                */
    /*----------------------------------------------------*/

    private void viewOrders() {

        List<Order> orders = new OrderRepository().findAll();

        if (orders.isEmpty()) {
            System.out.println("\nNo Orders Found.");
            return;
        }

        System.out.println("\n============= ORDERS =============");

        for (Order order : orders) {

            System.out.println("--------------------------------------------");
            System.out.println("Order Id        : " + order.getId());
            System.out.println("Customer Id     : " + order.getCustomerId());
            System.out.println("Restaurant Id   : " + order.getRestaurantId());
            System.out.println("Delivery Boy Id : "
                    + (order.getDeliveryBoyId() == null ? "Not Assigned" : order.getDeliveryBoyId()));
            System.out.println("Subtotal        : " + order.getSubtotal());
            System.out.println("Discount        : " + order.getDiscount());
            System.out.println("Delivery Charge : " + order.getDeliveryCharge());
            System.out.println("Payment Type    : " + order.getPaymentType());
            System.out.println("Status          : " + order.getOrderState().getStatus());
            System.out.println("Date            : " + order.getOrderDateTime());
        }

    }

    /*----------------------------------------------------*/

    private void traceOrder() {

        System.out.print("\nEnter Order Id : ");

        String orderId = sc.nextLine();

        List<OrderLog> logs =
                new OrderLogRepository().findByOrderId(orderId);

        if (logs.isEmpty()) {

            System.out.println("No Logs Found.");

            return;
        }

        System.out.println("\n============= ORDER TIMELINE =============");

        for (OrderLog log : logs) {

            System.out.println("-------------------------------------");
            System.out.println("Action     : " + log.getAction());
            System.out.println("Performed By : " + log.getActionBy());
            System.out.println("Date Time  : " + log.getActionDateTime());
        }

    }

}