package com.foodorder.exception;

public class DuplicateEmailException extends ApplicationException{
    public DuplicateEmailException(String message) {
        super(message);
    }
}
