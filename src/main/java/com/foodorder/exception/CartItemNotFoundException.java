package com.foodorder.exception;

public class CartItemNotFoundException extends ApplicationException{
    public CartItemNotFoundException(String message){
        super(message);
    }
}
