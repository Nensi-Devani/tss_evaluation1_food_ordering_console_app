package com.foodorder.exception;

public class MenuItemAlreadyExistsException extends ApplicationException{
    public MenuItemAlreadyExistsException(String message){
        super(message);
    }
}
