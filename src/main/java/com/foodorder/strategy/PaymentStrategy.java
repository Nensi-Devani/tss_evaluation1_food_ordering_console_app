package com.foodorder.strategy;

import com.foodorder.model.Payment;

public interface PaymentStrategy {
    void pay(Payment payment);
}