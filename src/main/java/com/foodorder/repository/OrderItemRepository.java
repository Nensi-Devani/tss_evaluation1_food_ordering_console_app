package com.foodorder.repository;

import java.util.ArrayList;
import java.util.List;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.IdConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.exception.OrderItemNotFoundException;
import com.foodorder.model.OrderItem;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

public class OrderItemRepository {

    public void save(OrderItem orderItem) {
        List<OrderItem> orderItems = FileUtil.readData(FileConstants.ORDER_ITEMS_FILE);

        orderItem.setId(IdGenerator.generateId(
                FileConstants.ORDER_ITEMS_FILE,
                IdConstants.ORDER_ITEM_ID_PREFIX));

        orderItems.add(orderItem);

        FileUtil.writeData(FileConstants.ORDER_ITEMS_FILE, orderItems);
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
