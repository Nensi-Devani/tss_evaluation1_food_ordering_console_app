package com.foodorder.state;

import com.foodorder.model.Order;

public class DeliveredState implements OrderState {
    @Override
    public void next(Order order) {
        System.out.println("Order is already delivered.");
    }

    @Override
    public String getStatus() {
        return "DELIVERED";
    }
}
