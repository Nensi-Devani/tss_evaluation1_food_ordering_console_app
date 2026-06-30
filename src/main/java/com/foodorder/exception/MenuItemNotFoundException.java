package com.foodorder.exception;

public class MenuItemNotFoundException extends ApplicationException {
    public MenuItemNotFoundException(String message) {
        super(message);
    }
}
