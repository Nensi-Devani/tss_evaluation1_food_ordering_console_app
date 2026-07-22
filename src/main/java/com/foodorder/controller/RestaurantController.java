package com.foodorder.controller;

import java.util.List;
import java.util.Scanner;

import com.foodorder.enums.*;
import com.foodorder.model.*;
import com.foodorder.repository.MenuItemRepository;
import com.foodorder.repository.OrderRepository;
import com.foodorder.repository.RestaurantRepository;
import com.foodorder.service.MenuItemService;
import com.foodorder.service.OrderService;

public class RestaurantController {
    private final Scanner sc = new Scanner(System.in);

    private final RestaurantRepository restaurantRepository = new RestaurantRepository();
    private final MenuItemRepository menuItemRepository = new MenuItemRepository();

    private final MenuItemService menuItemService = new MenuItemService();
    private final OrderService orderService = new OrderService();

    public void menu(User user) {
        while (true) {
            System.out.println("\n==============================");
            System.out.println("   RESTAURANT DASHBOARD");
            System.out.println("==============================");
            System.out.println("1. Dashboard");
            System.out.println("2. Manage Orders");
            System.out.println("3. Manage Menu");
            System.out.println("0. Logout");
            System.out.print("Choice : ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    dashboard(user);
                    break;

                case 2:
                    manageOrders(user);
                    break;

                case 3:
                    manageMenu(user);
                    break;

                case 0: return;

                default: System.out.println("Invalid Choice");
            }
        }
    }

    private void dashboard(User user) {
        Restaurant restaurant = restaurantRepository.findByOwnerId(user.getId());

        if (restaurant == null) {
            System.out.println("Restaurant not found.");
            return;
        }

        List<Order> orders = new OrderRepository().findByRestaurantId(restaurant.getId());

        int totalOrders = 0;
        double todayAmount = 0;

        for (Order order : orders) {
            if (order.getRestaurantId().equals(restaurant.getId())) {
                totalOrders++;

                todayAmount += order.getSubtotal()
                        - order.getDiscount()
                        + order.getDeliveryCharge();
            }
        }

        System.out.println("\n========== DASHBOARD ==========");
        System.out.println("Restaurant : " + restaurant.getName());
        System.out.println("Today's Orders : " + totalOrders);
        System.out.println("Today's Revenue : " + todayAmount);
    }

    private void manageOrders(User user) {
        while (true) {
            System.out.println("\n------ ORDER MANAGEMENT ------");
            System.out.println("1. View Orders");
            System.out.println("2. Accept Order");
            System.out.println("3. Mark Ready");
            System.out.println("4. Out For Delivery");
            System.out.println("0. Back");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    viewOrders(user);
                    break;

                case 2:
                    acceptOrder(user);
                    break;

                case 3:
                    readyOrder(user);
                    break;

                case 4:
                    outForDelivery(user);
                    break;

                case 0: return;
            }
        }
    }

    private void viewOrders(User user) {
        Restaurant restaurant = restaurantRepository.findByOwnerId(user.getId());
        List<Order> orders = new OrderRepository().findByRestaurantId(restaurant.getId());

        System.out.println();

        System.out.println("\n======================================================================================================");
        System.out.printf("%-5s %-12s %-15s %-12s %-18s%n",
                "No",
                "Order ID",
                "Customer",
                "Amount",
                "Status");
        System.out.println("======================================================================================================");

        int no = 1;

        for (Order order : orders) {
            if (!order.getRestaurantId().equals(restaurant.getId()))
                continue;

            double total = order.getSubtotal() - order.getDiscount() + order.getDeliveryCharge();

            System.out.printf("%-5d %-12s %-15s ₹%-11.2f %-18s%n",
                    no++,
                    order.getId(),
                    order.getCustomerId(),
                    total,
                    order.getOrderState().getStatus());
        }

        System.out.println("======================================================================================================");
    }

    private void acceptOrder(User user) {
        System.out.print("Order Id : ");
        String orderId = sc.nextLine();

        orderService.acceptOrder(orderId, user.getId());

        System.out.println("Order Accepted.");
    }

    private void readyOrder(User user) {
        System.out.print("Order Id : ");
        String orderId = sc.nextLine();

        orderService.markOrderReady(orderId, user.getId());

        System.out.println("Order Ready.");
    }

    private void outForDelivery(User user) {
        System.out.print("Order Id : ");
        String orderId = sc.nextLine();

        orderService.markOutForDelivery(orderId, user.getId());

        System.out.println("Delivery Boy Assigned.");
    }

    private void manageMenu(User user) {
        while (true) {
            System.out.println("\n------ MENU MANAGEMENT ------");
            System.out.println("1. View Menu");
            System.out.println("2. Add Menu Item");
            System.out.println("3. Update Menu Item");
            System.out.println("4. Update Discount");
            System.out.println("5. Activate Menu Item");
            System.out.println("6. Deactivate Menu Item");
            System.out.println("0. Back");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    viewMenu(user);
                    break;

                case 2:
                    addMenuItem(user);
                    break;

                case 3:
                    updateMenuItem();
                    break;

                case 4:
                    updateDiscount();
                    break;

                case 5:
                    activateMenuItem();
                    break;

                case 6:
                    deactivateMenuItem();
                    break;

                case 0: return;

                default: System.out.println("Invalid Choice");
            }
        }
    }

    private void viewMenu(User user) {
        Restaurant restaurant = restaurantRepository.findByOwnerId(user.getId());
        List<MenuItem> items = menuItemRepository.findByRestaurantId(restaurant.getId());

        if (items.isEmpty()) {
            System.out.println("No Menu Items Found.");
            return;
        }

        System.out.println("\n==================================================================================================================================");
        System.out.printf("%-5s %-10s %-25s %-12s %-12s %-12s %-18s %-12s%n",
                "No",
                "ID",
                "Name",
                "Price",
                "Discount",
                "Type",
                "Category",
                "Status");
        System.out.println("==================================================================================================================================");

        int no = 1;

        for (MenuItem item : items) {
            System.out.printf("%-5d %-10s %-25s ₹%-11.2f %-12s %-12s %-18s %-12s%n",
                    no++,
                    item.getId(),
                    item.getName(),
                    item.getPrice(),
                    String.format("%.2f%%", item.getDiscount()),
                    item.getFoodType(),
                    item.getFoodCategory(),
                    item.getStatus());
        }

        System.out.println("==================================================================================================================================");
    }

    private void addMenuItem(User user) {
        Restaurant restaurant = restaurantRepository.findByOwnerId(user.getId());

        System.out.print("Name : ");
        String name = sc.nextLine();

        System.out.print("Price : ");
        double price = Double.parseDouble(sc.nextLine());

        System.out.print("Discount : ");
        double discount = Double.parseDouble(sc.nextLine());

        System.out.println("Food Type");
        System.out.println("1. VEG");
        System.out.println("2. NON_VEG");

        FoodType foodType =
                Integer.parseInt(sc.nextLine()) == 1
                        ? FoodType.VEG
                        : FoodType.NON_VEG;

        System.out.println("Food Category");
        FoodCategory[] categories = FoodCategory.values();

        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }

        int categoryChoice = Integer.parseInt(sc.nextLine());

        FoodCategory category = categories[categoryChoice - 1];

        MenuItem item = new MenuItem(
                restaurant.getId(),
                name,
                price,
                discount,
                foodType,
                category,
                Status.ACTIVE
        );

        menuItemService.addMenuItem(item);

        System.out.println("Menu Item Added Successfully.");
    }

    private void updateMenuItem() {
        System.out.print("Menu Item Id : ");
        String id = sc.nextLine();

        MenuItem item = menuItemRepository.findById(id);

        System.out.print("New Name : ");
        item.setName(sc.nextLine());

        System.out.print("New Price : ");
        item.setPrice(Double.parseDouble(sc.nextLine()));

        menuItemRepository.update(item);

        System.out.println("Menu Item Updated.");
    }

    private void updateDiscount() {
        System.out.print("Menu Item Id : ");
        String id = sc.nextLine();

        MenuItem item = menuItemRepository.findById(id);

        System.out.print("Discount : ");
        item.setDiscount(Double.parseDouble(sc.nextLine()));

        menuItemRepository.update(item);

        System.out.println("Discount Updated.");
    }

    private void activateMenuItem() {
        System.out.print("Menu Item Id : ");
        String id = sc.nextLine();

        MenuItem item = menuItemRepository.findById(id);
        item.setStatus(Status.ACTIVE);

        menuItemRepository.update(item);

        System.out.println("Menu Item Activated.");
    }

    private void deactivateMenuItem() {
        System.out.print("Menu Item Id : ");
        String id = sc.nextLine();

        MenuItem item = menuItemRepository.findById(id);

        item.setStatus(Status.INACTIVE);

        menuItemRepository.update(item);

        System.out.println("Menu Item Deactivated.");
    }

}