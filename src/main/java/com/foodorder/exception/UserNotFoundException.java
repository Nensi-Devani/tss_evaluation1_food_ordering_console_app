package com.foodorder.exception;

public class UserNotFoundException extends ApplicationException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
