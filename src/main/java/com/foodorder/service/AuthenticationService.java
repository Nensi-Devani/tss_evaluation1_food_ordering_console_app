package com.foodorder.service;

import com.foodorder.model.User;

public class AuthenticationService {
    private final UserService userService = new UserService();

    public User login(String email, String password) {
        return userService.login(email, password);
    }

    public void register(User user) {
        userService.register(user);
    }

    public void logout() {
        System.out.println("\nLogged out successfully.");
    }
}