package com.foodorder.strategy;

import com.foodorder.enums.PaymentStatus;
import com.foodorder.model.Payment;

public class UpiPaymentStrategy implements PaymentStrategy {
    @Override
    public void pay(Payment payment) {
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
    }
}