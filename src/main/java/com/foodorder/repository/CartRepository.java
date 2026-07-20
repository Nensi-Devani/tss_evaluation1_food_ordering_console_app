package com.foodorder.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.IdConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.database.DatabaseConnection;
import com.foodorder.exception.CartNotFoundException;
import com.foodorder.model.Cart;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

public class CartRepository {
    Connection connection;
    PreparedStatement preparedStatement;

    public CartRepository(){
        connection = DatabaseConnection
                        .getInstance()
                        .getConnection();
    }

    public void save(Cart cart) {
        String query = "INSERT INTO carts (user_id, restaurant_id) VALUES (?, ?)";

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, Long.parseLong(cart.getCustomerId()));
            preparedStatement.setLong(2, Long.parseLong(cart.getRestaurantId()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void update(Cart cart) {
        List<Cart> carts = FileUtil.readData(FileConstants.CARTS_FILE);

        for (int i = 0; i < carts.size(); i++) {
            if (carts.get(i).getId().equals(cart.getId())) {
                carts.set(i, cart);

                FileUtil.writeData(FileConstants.CARTS_FILE, carts);
                return;
            }
        }

        throw new CartNotFoundException(MessageConstants.CART_NOT_FOUND);
    }

    public Cart findById(String id) {
        List<Cart> carts = FileUtil.readData(FileConstants.CARTS_FILE);

        for (Cart cart : carts) {
            if (cart.getId().equals(id))
                return cart;
        }

        throw new CartNotFoundException(MessageConstants.CART_NOT_FOUND);
    }

    public Cart findByCustomerId(String customerId) {
        String query = "SELECT * FROM carts WHERE user_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, Long.parseLong(customerId));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Cart cart = new Cart();

                cart.setId(String.valueOf(resultSet.getLong("cart_id")));
                cart.setCustomerId(String.valueOf(resultSet.getLong("user_id")));
                cart.setRestaurantId(String.valueOf(resultSet.getLong("restaurant_id")));

                return cart;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }

    public List<Cart> findAll() {
        return FileUtil.readData(FileConstants.CARTS_FILE);
    }
}
