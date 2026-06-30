package com.foodorder.exception;

public class UserAddressNotFoundException extends ApplicationException{
    public UserAddressNotFoundException(String message) {
        super(message);
    }
}
