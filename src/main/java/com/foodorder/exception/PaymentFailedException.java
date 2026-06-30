package com.foodorder.exception;

public class PaymentFailedException extends ApplicationException {
    public PaymentFailedException(String message) {
        super(message);
    }
}
