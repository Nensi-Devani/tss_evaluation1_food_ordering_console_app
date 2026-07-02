package com.foodorder.state;

import com.foodorder.model.Order;

import java.io.Serializable;

public interface OrderState extends Serializable {
    void next(Order order);
    String getStatus();
}
