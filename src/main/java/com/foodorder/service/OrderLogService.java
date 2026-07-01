package com.foodorder.service;

import java.util.List;

import com.foodorder.model.OrderLog;
import com.foodorder.repository.OrderLogRepository;

public class OrderLogService {
    private final OrderLogRepository orderLogRepository = new OrderLogRepository();

    public void addLog(OrderLog log) {
        orderLogRepository.save(log);
    }

    public List<OrderLog> getLogsByOrderId(String orderId) {
        return orderLogRepository.findByOrderId(orderId);
    }
}