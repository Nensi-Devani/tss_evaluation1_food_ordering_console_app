package com.foodorder.repository;

import java.util.ArrayList;
import java.util.List;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.IdConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.enums.OrderStatus;
import com.foodorder.exception.OrderNotFoundException;
import com.foodorder.model.Order;
import com.foodorder.state.OrderState;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

public class OrderRepository {

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

    public List<Order> findAll() {
        return FileUtil.readData(FileConstants.ORDERS_FILE);
    }
}
