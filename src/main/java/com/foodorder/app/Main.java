package com.foodorder.app;

import com.foodorder.config.ApplicationConfig;
import com.foodorder.controller.LoginController;

public class Main {
    public static void main(String[] args) {
        ApplicationConfig.init();

        LoginController loginController = new LoginController();

        while (true) {
            loginController.start();
        }
    }
}