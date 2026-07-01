package com.foodorder.state;

import com.foodorder.model.Order;

public class OutForDeliveryState implements OrderState {
    @Override
    public void next(Order order) {
        order.setOrderState(new DeliveredState());
    }

    @Override
    public String getStatus() {
        return "OUT_FOR_DELIVERY";
    }
}
