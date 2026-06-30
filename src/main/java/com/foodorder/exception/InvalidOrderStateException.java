package com.foodorder.exception;

public class InvalidOrderStateException extends ApplicationException {
    public InvalidOrderStateException(String message) {
        super(message);
    }
}
