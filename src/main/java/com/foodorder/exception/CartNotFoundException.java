package com.foodorder.exception;

public class CartNotFoundException extends ApplicationException {
    public CartNotFoundException(String message) {
        super(message);
    }
}
