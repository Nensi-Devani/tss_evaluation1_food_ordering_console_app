package com.foodorder.exception;

public class OrderNotFoundException extends ApplicationException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
