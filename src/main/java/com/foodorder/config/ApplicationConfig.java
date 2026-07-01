package com.foodorder.config;

import com.foodorder.facade.CheckoutFacade;

public class ApplicationConfig {
    public static CheckoutFacade checkoutFacade;

    public static void init() {
        checkoutFacade = new CheckoutFacade();

        System.out.println("System Initialized...");
    }
}
