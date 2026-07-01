package com.foodorder.state;

import com.foodorder.model.Order;

public interface OrderState {
    void next(Order order);
    String getStatus();
}
