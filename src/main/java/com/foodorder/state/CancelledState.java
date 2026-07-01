package com.foodorder.state;

import com.foodorder.model.Order;

public class CancelledState implements OrderState {
    @Override
    public void next(Order order) {
        System.out.println("Order has been cancelled. No further state transitions are allowed.");
    }

    @Override
    public String getStatus() {
        return "CANCELLED";
    }
}
