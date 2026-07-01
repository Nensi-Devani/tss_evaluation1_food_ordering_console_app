package com.foodorder.config;

import java.io.File;
import java.io.IOException;

import com.foodorder.constants.FileConstants;
import com.foodorder.facade.CheckoutFacade;

public class ApplicationConfig {
    public static CheckoutFacade checkoutFacade;

    public static void init() {
        checkoutFacade = new CheckoutFacade();

        createFile(FileConstants.USERS_FILE);
        createFile(FileConstants.RESTAURANTS_FILE);
        createFile(FileConstants.MENU_ITEMS_FILE);
        createFile(FileConstants.CARTS_FILE);
        createFile(FileConstants.CART_ITEMS_FILE);
        createFile(FileConstants.ORDERS_FILE);
        createFile(FileConstants.ORDER_ITEMS_FILE);
        createFile(FileConstants.ORDER_LOGS_FILE);
        createFile(FileConstants.ORDER_OTPS_FILE);
        createFile(FileConstants.PAYMENTS_FILE);
        createFile(FileConstants.DISCOUNTS_FILE);

        System.out.println("System Initialized...");
    }

    private static void createFile(String path) {
        try {
            File file = new File(path);

            if (!file.exists()) {
                file.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//package com.foodorder.config;
//
//import com.foodorder.facade.CheckoutFacade;
//
//public class ApplicationConfig {
//    public static CheckoutFacade checkoutFacade;
//
//    public static void init() {
//        checkoutFacade = new CheckoutFacade();
//        System.out.println("System Initialized...");
//    }
//}
