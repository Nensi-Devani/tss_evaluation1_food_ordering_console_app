package com.foodorder.exception;

public class EmptyCartException extends ApplicationException {
    public EmptyCartException(String message) {
        super(message);
    }
}
