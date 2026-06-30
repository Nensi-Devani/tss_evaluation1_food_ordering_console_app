package com.foodorder.model;

import com.foodorder.common.Identifiable;
import com.foodorder.enums.Status;

import java.io.Serializable;

public class Discount implements Serializable, Identifiable {
    private static final long serialVersionUID = 1L;

    private String id;
    private double minimumAmount;
    private double discountPercentage;
    private Status status;

    public Discount(double minimumAmount, double discountPercentage, Status status) {
        this.minimumAmount = minimumAmount;
        this.discountPercentage = discountPercentage;
        this.status = status;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(double minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
