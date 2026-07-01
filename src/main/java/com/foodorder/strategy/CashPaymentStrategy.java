package com.foodorder.strategy;

import com.foodorder.enums.PaymentStatus;
import com.foodorder.model.Payment;
public class CashPaymentStrategy implements PaymentStrategy {

    @Override
    public void pay(Payment payment) {
        payment.setPaymentStatus(PaymentStatus.PENDING);
    }
}