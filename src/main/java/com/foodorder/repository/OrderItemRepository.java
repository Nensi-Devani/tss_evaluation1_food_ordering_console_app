package com.foodorder.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.IdConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.database.DatabaseConnection;
import com.foodorder.exception.OrderItemNotFoundException;
import com.foodorder.model.OrderItem;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

public class OrderItemRepository {
    Connection connection;
    PreparedStatement preparedStatement;

    public OrderItemRepository(){
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public void save(OrderItem orderItem)   {
        String query = "INSERT INTO order_items (order_id, menu_item_id, price, discount, quantity) VALUES (?, ?, ?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, Long.parseLong(orderItem.getOrderId()));
            preparedStatement.setLong(2, Long.parseLong(orderItem.getMenuItemId()));
            preparedStatement.setDouble(3, orderItem.getPrice());
            preparedStatement.setDouble(4, orderItem.getDiscount());
            preparedStatement.setInt(5, orderItem.getQuantity());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void update(OrderItem orderItem) {
        List<OrderItem> orderItems = FileUtil.readData(FileConstants.ORDER_ITEMS_FILE);

        for (int i = 0; i < orderItems.size(); i++) {
            if (orderItems.get(i).getId().equals(orderItem.getId())) {
                orderItems.set(i, orderItem);

                FileUtil.writeData(FileConstants.ORDER_ITEMS_FILE, orderItems);
                return;
            }
        }

        throw new OrderItemNotFoundException(MessageConstants.ORDER_ITEM_NOT_FOUND);
    }

    public OrderItem findById(String id) {
        List<OrderItem> orderItems = FileUtil.readData(FileConstants.ORDER_ITEMS_FILE);

        for (OrderItem orderItem : orderItems) {
            if (orderItem.getId().equals(id))
                return orderItem;
        }

        throw new OrderItemNotFoundException(MessageConstants.ORDER_ITEM_NOT_FOUND);
    }

    public List<OrderItem> findByOrderId(String orderId) {
        List<OrderItem> filteredOrderItems = new ArrayList<>();

        List<OrderItem> orderItems = FileUtil.readData(FileConstants.ORDER_ITEMS_FILE);

        for (OrderItem orderItem : orderItems) {
            if (orderItem.getOrderId().equals(orderId))
                filteredOrderItems.add(orderItem);
        }

        return filteredOrderItems;
    }

    public List<OrderItem> findAll() {
        return FileUtil.readData(FileConstants.ORDER_ITEMS_FILE);
    }

}
