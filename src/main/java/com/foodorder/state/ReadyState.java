package com.foodorder.state;

import com.foodorder.model.Order;

public class ReadyState implements OrderState {
    @Override
    public void next(Order order) {
        order.setOrderState(new OutForDeliveryState());
    }

    @Override
    public String getStatus() {
        return "READY";
    }
}
