package com.foodorder.model;

import com.foodorder.common.Identifiable;
import com.foodorder.enums.PaymentStatus;
import com.foodorder.enums.PaymentType;

import java.io.Serializable;

public class Payment implements Serializable, Identifiable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String orderId;
    private double amount;
    private PaymentType paymentType;
    private PaymentStatus paymentStatus;

    public Payment(String orderId, double amount, PaymentType paymentType, PaymentStatus paymentStatus) {
        this.orderId = orderId;
        this.amount = amount;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
