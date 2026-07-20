package com.foodorder.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.foodorder.constants.FileConstants;
import com.foodorder.database.DatabaseConnection;
import com.foodorder.enums.PaymentType;
import com.foodorder.model.Order;
import com.foodorder.state.*;
import com.foodorder.util.FileUtil;

public class OrderRepository {
    Connection connection;
    PreparedStatement preparedStatement;

    public OrderRepository(){
        connection = DatabaseConnection
                        .getInstance()
                        .getConnection();
    }

    public void save(Order order) {
        String query = "INSERT INTO orders (customer_id, restaurant_id, delivery_boy_id, order_date_time, sub_total, discount, delivery_charge, payment_type, order_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setLong(1, Long.parseLong(order.getCustomerId()));
            preparedStatement.setLong(2, Long.parseLong(order.getRestaurantId()));
            preparedStatement.setObject(3, null);
            preparedStatement.setTimestamp(4, Timestamp.valueOf(order.getOrderDateTime()));
            preparedStatement.setDouble(5, order.getSubtotal());
            preparedStatement.setDouble(6, order.getDiscount());
            preparedStatement.setDouble(7, order.getDeliveryCharge());
            preparedStatement.setString(8, order.getPaymentType().name());
            preparedStatement.setString(9, "PLACED");

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                order.setId(String.valueOf(resultSet.getLong(1)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void update(Order order) {
        String query = "UPDATE orders SET sub_total=?, discount=?, delivery_charge=?, payment_type=?, order_status=? WHERE order_id=?";

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setDouble(1, order.getSubtotal());
            preparedStatement.setDouble(2, order.getDiscount());
            preparedStatement.setDouble(3, order.getDeliveryCharge());
            preparedStatement.setString(4, order.getPaymentType().name());
            preparedStatement.setString(5, order.getOrderState().getStatus());
            preparedStatement.setLong(6, Long.parseLong(order.getId()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Order findById(String id) {
        String query = "SELECT * FROM orders WHERE order_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, Long.parseLong(id));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Order order = new Order();

                order.setId(String.valueOf(resultSet.getLong("order_id")));
                order.setCustomerId(String.valueOf(resultSet.getLong("customer_id")));
                order.setRestaurantId(String.valueOf(resultSet.getLong("restaurant_id")));

                long deliveryBoyId = resultSet.getLong("delivery_boy_id");

                if (!resultSet.wasNull()) {
                    order.setDeliveryBoyId(
                            String.valueOf(deliveryBoyId)
                    );
                }

                order.setOrderDateTime(resultSet.getTimestamp("order_date_time").toLocalDateTime());
                order.setSubtotal(resultSet.getDouble("sub_total"));
                order.setDiscount(resultSet.getDouble("discount"));
                order.setDeliveryCharge(resultSet.getDouble("delivery_charge"));
                order.setPaymentType(PaymentType.valueOf(resultSet.getString("payment_type")));
                order.setOrderState(getOrderState(resultSet.getString("order_status")));

                return order;
            }
        } catch(SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }

    public List<Order> findByCustomerId(String customerId) {
        List<Order> customerOrders = new ArrayList<>();

        String query = "SELECT * FROM orders WHERE customer_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, Long.parseLong(customerId));
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

                customerOrders.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return customerOrders;
    }

    public List<Order> findByRestaurantId(String restaurantId) {
        List<Order> orders = new ArrayList<>();

        String query = "SELECT * FROM orders WHERE restaurant_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, Long.parseLong(restaurantId));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order();

                order.setId(String.valueOf(resultSet.getLong("order_id")));
                order.setCustomerId(String.valueOf(resultSet.getLong("customer_id")));
                order.setRestaurantId(String.valueOf(resultSet.getLong("restaurant_id")));

                long deliveryBoyId = resultSet.getLong("delivery_boy_id");

                if (!resultSet.wasNull()) {
                    order.setDeliveryBoyId(
                            String.valueOf(deliveryBoyId)
                    );
                }

                order.setOrderDateTime(resultSet.getTimestamp("order_date_time").toLocalDateTime());
                order.setSubtotal(resultSet.getDouble("sub_total"));
                order.setDiscount(resultSet.getDouble("discount"));
                order.setDeliveryCharge(resultSet.getDouble("delivery_charge"));
                order.setPaymentType(PaymentType.valueOf(resultSet.getString("payment_type")));
                order.setOrderState(getOrderState(resultSet.getString("order_status")));

                orders.add(order);
            }
        } catch(SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return orders;
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
