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
import com.foodorder.enums.PaymentType;
import com.foodorder.exception.OrderNotFoundException;
import com.foodorder.model.Order;
import com.foodorder.state.*;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

public class OrderRepository {
    Connection connection;
    PreparedStatement preparedStatement;

    public OrderRepository(){
        connection = DatabaseConnection
                        .getInstance()
                        .getConnection();
    }

    public void save(Order order) {
        List<Order> orders = FileUtil.readData(FileConstants.ORDERS_FILE);

        order.setId(IdGenerator.generateId(
                FileConstants.ORDERS_FILE,
                IdConstants.ORDER_ID_PREFIX));

        orders.add(order);

        FileUtil.writeData(FileConstants.ORDERS_FILE, orders);
    }

    public void update(Order order) {
        List<Order> orders = FileUtil.readData(FileConstants.ORDERS_FILE);

        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId().equals(order.getId())) {
                orders.set(i, order);

                FileUtil.writeData(FileConstants.ORDERS_FILE, orders);
                return;
            }
        }

        throw new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND);
    }

    public Order findById(String id) {
        List<Order> orders = FileUtil.readData(FileConstants.ORDERS_FILE);

        for (Order order : orders) {
            if (order.getId().equals(id))
                return order;
        }

        throw new OrderNotFoundException(MessageConstants.ORDER_NOT_FOUND);
    }

    public List<Order> findByCustomerId(String customerId) {
        List<Order> customerOrders = new ArrayList<>();

        List<Order> orders = FileUtil.readData(FileConstants.ORDERS_FILE);

        for (Order order : orders) {
            if (order.getCustomerId().equals(customerId))
                customerOrders.add(order);
        }

        return customerOrders;
    }

    public List<Order> findByRestaurantId(String restaurantId) {
        List<Order> restaurantOrders = new ArrayList<>();

        List<Order> orders = FileUtil.readData(FileConstants.ORDERS_FILE);

        for (Order order : orders) {
            if (order.getRestaurantId().equals(restaurantId))
                restaurantOrders.add(order);
        }

        return restaurantOrders;
    }

    public List<Order> findByDeliveryBoyId(String deliveryBoyId) {
        List<Order> deliveryOrders = new ArrayList<>();

        List<Order> orders = FileUtil.readData(FileConstants.ORDERS_FILE);

        for (Order order : orders) {
            if (deliveryBoyId.equals(order.getDeliveryBoyId()))
                deliveryOrders.add(order);
        }

        return deliveryOrders;
    }

    public List<Order> findByOrderState(OrderState orderState) {
        List<Order> filteredOrders = new ArrayList<>();

        List<Order> orders = FileUtil.readData(FileConstants.ORDERS_FILE);

        for (Order order : orders) {
            if (order.getOrderState() == orderState)
                filteredOrders.add(order);
        }

        return filteredOrders;
    }

    private OrderState getOrderState(String state) {
        switch (state) {
            case "PLACED":
                return new PlacedState();

            case "PREPARING":
                return new PreparingState();

            case "READY":
                return new ReadyState();

            case "OUT_FOR_DELIVERY":
                return new OutForDeliveryState();

            case "DELIVERED":
                return new DeliveredState();

            case "CANCELLED":
                return new CancelledState();

            default:
                throw new RuntimeException("Invalid order state : " + state);
        }
    }

    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();

        String query = "SELECT * FROM orders";

        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order();

                order.setId(String.valueOf(resultSet.getLong("order_id")));
                order.setCustomerId(String.valueOf(resultSet.getLong("customer_id")));
                order.setRestaurantId(String.valueOf(resultSet.getLong("restaurant_id")));

                long deliveryBoyId = resultSet.getLong("delivery_boy_id");
                if (!resultSet.wasNull()) {
                    order.setDeliveryBoyId(String.valueOf(deliveryBoyId));
                }

                order.setOrderDateTime(resultSet.getTimestamp("order_date_time").toLocalDateTime());
                order.setSubtotal(resultSet.getDouble("sub_total"));
                order.setDiscount(resultSet.getDouble("discount"));
                order.setDeliveryCharge(resultSet.getDouble("delivery_charge"));
                order.setPaymentType(PaymentType.valueOf(resultSet.getString("payment_type")));
                order.setOrderState(getOrderState(resultSet.getString("order_status")));

                orders.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return orders;
    }
}
