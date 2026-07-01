package com.foodorder.factory;

import com.foodorder.enums.PaymentType;
import com.foodorder.strategy.CashPaymentStrategy;
import com.foodorder.strategy.PaymentStrategy;
import com.foodorder.strategy.UpiPaymentStrategy;

public class PaymentFactory {
    public static PaymentStrategy getPaymentStrategy(PaymentType paymentType) {
        switch (paymentType) {
            case CASH:
                return new CashPaymentStrategy();

            case UPI:
                return new UpiPaymentStrategy();

            default:
                throw new IllegalArgumentException("Invalid Payment Type");
        }
    }
}