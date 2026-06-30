package com.foodorder.exception;

public class OTPMismatchException extends ApplicationException {
    public OTPMismatchException(String message) {
        super(message);
    }
}
