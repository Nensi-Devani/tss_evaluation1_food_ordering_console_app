package com.foodorder.exception;

public class OrderItemNotFoundException extends ApplicationException{
    public OrderItemNotFoundException(String message){
        super(message);
    }
}
