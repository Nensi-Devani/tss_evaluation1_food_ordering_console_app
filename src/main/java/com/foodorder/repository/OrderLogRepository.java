package com.foodorder.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.IdConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.database.DatabaseConnection;
import com.foodorder.enums.OrderLogAction;
import com.foodorder.exception.OrderLogNotFoundException;
import com.foodorder.model.OrderLog;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

public class OrderLogRepository {
    Connection connection;
    PreparedStatement preparedStatement;

    public OrderLogRepository(){
        connection = DatabaseConnection
                        .getInstance()
                        .getConnection();
    }

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

        String query = "SELECT * FROM order_logs WHERE order_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, Long.parseLong(orderId));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                OrderLog orderLog = new OrderLog();

                orderLog.setId(String.valueOf(resultSet.getLong("order_log_id")));
                orderLog.setOrderId(String.valueOf(resultSet.getLong("order_id")));
                orderLog.setAction(OrderLogAction.valueOf(resultSet.getString("action")));
                orderLog.setActionBy(String.valueOf(resultSet.getLong("action_taken_by")));
                orderLog.setActionDateTime(
                        resultSet.getTimestamp("action_date_time").toLocalDateTime()
                );

                filteredLogs.add(orderLog);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return filteredLogs;
    }

    public List<OrderLog> findAll() {
        return FileUtil.readData(FileConstants.ORDER_LOGS_FILE);
    }

}
