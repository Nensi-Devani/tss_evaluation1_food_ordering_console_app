package com.foodorder.state;

import com.foodorder.model.Order;

public class PlacedState implements OrderState {
    @Override
    public void next(Order order) {
        order.setOrderState(new PreparingState());
    }

    @Override
    public String getStatus() {
        return "PLACED";
    }
}
