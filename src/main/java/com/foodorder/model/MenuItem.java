package com.foodorder.model;

import com.foodorder.common.Identifiable;
import com.foodorder.enums.FoodCategory;
import com.foodorder.enums.FoodType;
import com.foodorder.enums.Status;

import java.io.Serializable;

public class MenuItem implements Serializable, Identifiable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String restaurantId;
    private String name;
    private double price;
    private double discount;
    private FoodType foodType;
    private FoodCategory foodCategory;
    private Status status;

    public MenuItem(){
    }

    public MenuItem(String restaurantId, String name, double price, double discount, FoodType foodType, FoodCategory foodCategory, Status status) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.foodType = foodType;
        this.foodCategory = foodCategory;
        this.status = status;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    public FoodCategory getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(FoodCategory foodCategory) {
        this.foodCategory = foodCategory;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
