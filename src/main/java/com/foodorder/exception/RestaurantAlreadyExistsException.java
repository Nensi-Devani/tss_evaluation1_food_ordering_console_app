package com.foodorder.exception;

public class RestaurantAlreadyExistsException extends ApplicationException{
    public RestaurantAlreadyExistsException(String message){
        super(message);
    }
}
