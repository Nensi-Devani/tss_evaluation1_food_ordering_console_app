package com.foodorder.config;

import com.foodorder.database.DatabaseConnection;
import com.foodorder.enums.Role;
import com.foodorder.enums.Status;
import com.foodorder.facade.CheckoutFacade;
import com.foodorder.model.User;
import com.foodorder.repository.UserRepository;

public class ApplicationConfig {
    public static CheckoutFacade checkoutFacade;

    public static void init() {
        checkoutFacade = new CheckoutFacade();

        DatabaseConnection.getInstance();

        initializeAdmin();
    }

    private static void initializeAdmin() {
        UserRepository userRepository = new UserRepository();

        User admin = userRepository.findByEmail("admin@gmail.com");

        if (admin == null) {
            admin = new User();

            admin.setName("Nensi-Admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword("123456");
            admin.setRole(Role.ADMIN);
            admin.setStatus(Status.ACTIVE);

            userRepository.save(admin);

            System.out.println("Default Admin Created.");
        }
    }
}
