package com.foodorder.exception;

public class OrderCancellationNotAllowedException extends ApplicationException {
    public OrderCancellationNotAllowedException(String message) {
        super(message);
    }
}
