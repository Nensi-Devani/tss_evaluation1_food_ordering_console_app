package com.foodorder.state;

import com.foodorder.model.Order;

public class PreparingState implements OrderState{
    @Override
    public void next(Order order) {
        order.setOrderState(new ReadyState());
    }

    @Override
    public String getStatus() {
        return "PREPARING";
    }
}
