package com.foodorder.exception;

public class DiscountNotFoundException extends ApplicationException{
    public DiscountNotFoundException(String message){
        super(message);
    }
}
