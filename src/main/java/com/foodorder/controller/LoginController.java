package com.foodorder.controller;

import java.util.Scanner;

import com.foodorder.enums.Role;
import com.foodorder.model.User;
import com.foodorder.service.AuthenticationService;

public class LoginController {
    private final AuthenticationService authService = new AuthenticationService();
    private final Scanner sc = new Scanner(System.in);

    public void start() {
        System.out.println("1. Login");
        System.out.println("2. Register");

        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) login();
        else register();
    }

    private void login() {
        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        User user = authService.login(email, password);

        routeByRole(user);
    }

    private void register() {
        User user = new User();

        System.out.print("Name: ");
        user.setName(sc.nextLine());

        System.out.print("Email: ");
        user.setEmail(sc.nextLine());

        System.out.print("Password: ");
        user.setPassword(sc.nextLine());

        System.out.print("Role (CUSTOMER/RESTAURANT/DELIVERY_BOY): ");
        user.setRole(Role.valueOf(sc.nextLine()));

        authService.register(user);

        System.out.println("Registered successfully!");
    }

    private void routeByRole(User user) {
        switch (user.getRole()) {
            case ADMIN:
                new AdminController().menu(user);
                break;

            case CUSTOMER:
                new CustomerController().menu(user);
                break;

            case RESTAURANT:
                new RestaurantController().menu(user);
                break;

            case DELIVERY_BOY:
                new DeliveryBoyController().menu(user);
                break;
        }
    }
}