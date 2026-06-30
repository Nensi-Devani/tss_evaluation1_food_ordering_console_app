package com.foodorder.model;

import com.foodorder.common.Identifiable;

import java.io.Serializable;
import java.time.LocalDateTime;

public class OrderOTP implements Serializable, Identifiable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String orderId;
    private String otp;
    private LocalDateTime generatedAt;

    public OrderOTP(String orderId, String otp, LocalDateTime generatedAt) {
        this.orderId = orderId;
        this.otp = otp;
        this.generatedAt = generatedAt;
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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}
