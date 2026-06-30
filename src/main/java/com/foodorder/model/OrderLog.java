package com.foodorder.model;

import com.foodorder.enums.OrderLogAction;

import java.io.Serializable;
import java.time.LocalDateTime;

public class OrderLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String orderId;
    private OrderLogAction action;
    private String actionBy;
    private LocalDateTime actionDateTime;

    public OrderLog(String orderId, OrderLogAction action, String actionBy, LocalDateTime actionDateTime) {
        this.orderId = orderId;
        this.action = action;
        this.actionBy = actionBy;
        this.actionDateTime = actionDateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public OrderLogAction getAction() {
        return action;
    }

    public void setAction(OrderLogAction action) {
        this.action = action;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public LocalDateTime getActionDateTime() {
        return actionDateTime;
    }

    public void setActionDateTime(LocalDateTime actionDateTime) {
        this.actionDateTime = actionDateTime;
    }
}
