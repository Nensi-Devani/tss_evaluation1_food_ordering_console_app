package com.foodorder.repository;

import java.util.ArrayList;
import java.util.List;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.IdConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.exception.OrderLogNotFoundException;
import com.foodorder.model.OrderLog;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

public class OrderLogRepository {

    public void save(OrderLog orderLog) {
        List<OrderLog> orderLogs = FileUtil.readData(FileConstants.ORDER_LOGS_FILE);

        orderLog.setId(IdGenerator.generateId(
                FileConstants.ORDER_LOGS_FILE,
                IdConstants.ORDER_LOG_ID_PREFIX));

        orderLogs.add(orderLog);

        FileUtil.writeData(FileConstants.ORDER_LOGS_FILE, orderLogs);
    }

    public void update(OrderLog orderLog) {
        List<OrderLog> orderLogs = FileUtil.readData(FileConstants.ORDER_LOGS_FILE);

        for (int i = 0; i < orderLogs.size(); i++) {
            if (orderLogs.get(i).getId().equals(orderLog.getId())) {
                orderLogs.set(i, orderLog);

                FileUtil.writeData(FileConstants.ORDER_LOGS_FILE, orderLogs);
                return;
            }
        }

        throw new OrderLogNotFoundException(MessageConstants.ORDER_LOG_NOT_FOUND);
    }

    public OrderLog findById(String id) {
        List<OrderLog> orderLogs = FileUtil.readData(FileConstants.ORDER_LOGS_FILE);

        for (OrderLog orderLog : orderLogs) {
            if (orderLog.getId().equals(id))
                return orderLog;
        }

        throw new OrderLogNotFoundException(MessageConstants.ORDER_LOG_NOT_FOUND);
    }

    public List<OrderLog> findByOrderId(String orderId) {
        List<OrderLog> filteredLogs = new ArrayList<>();

        List<OrderLog> orderLogs = FileUtil.readData(FileConstants.ORDER_LOGS_FILE);

        for (OrderLog orderLog : orderLogs) {
            if (orderLog.getOrderId().equals(orderId))
                filteredLogs.add(orderLog);
        }

        return filteredLogs;
    }

    public List<OrderLog> findAll() {
        return FileUtil.readData(FileConstants.ORDER_LOGS_FILE);
    }

}
