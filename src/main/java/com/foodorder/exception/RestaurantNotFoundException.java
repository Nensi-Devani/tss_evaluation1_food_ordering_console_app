package com.foodorder.exception;

public class RestaurantNotFoundException extends ApplicationException {
    public RestaurantNotFoundException(String message) {
        super(message);
    }
}
