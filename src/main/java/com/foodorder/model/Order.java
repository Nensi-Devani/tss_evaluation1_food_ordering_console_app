package com.foodorder.model;

import com.foodorder.common.Identifiable;
import com.foodorder.enums.PaymentType;
import com.foodorder.state.OrderState;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Order implements Serializable, Identifiable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String customerId;
    private String restaurantId;
    private String deliveryBoyId;
    private String deliveryAddressId;
    private LocalDateTime orderDateTime;
    private double subtotal;
    private double discount;
    private double deliveryCharge;
    private PaymentType paymentType;
    private OrderState orderState;

    public Order(String customerId, String restaurantId, String deliveryBoyId, LocalDateTime orderDateTime, double subtotal, double discount, double deliveryCharge, PaymentType paymentType, OrderState orderState) {
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.deliveryBoyId = deliveryBoyId;
        this.orderDateTime = orderDateTime;
        this.subtotal = subtotal;
        this.discount = discount;
        this.deliveryCharge = deliveryCharge;
        this.paymentType = paymentType;
        this.orderState = orderState;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getDeliveryBoyId() {
        return deliveryBoyId;
    }

    public void setDeliveryBoyId(String deliveryBoyId) {
        this.deliveryBoyId = deliveryBoyId;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public String getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(String deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }
}
