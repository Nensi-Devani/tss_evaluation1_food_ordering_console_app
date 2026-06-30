package com.foodorder.model;

import com.foodorder.common.Identifiable;

import java.io.Serializable;

public class OrderItem implements Serializable, Identifiable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String orderId;
    private String menuItemId;
    private double price;
    private double discount;
    private int quantity;

    public OrderItem(String orderId, String menuItemId, double price, double discount, int quantity) {
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
