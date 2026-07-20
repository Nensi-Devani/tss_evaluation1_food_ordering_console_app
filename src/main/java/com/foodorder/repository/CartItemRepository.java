package com.foodorder.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.database.DatabaseConnection;
import com.foodorder.exception.CartItemNotFoundException;
import com.foodorder.model.CartItem;
import com.foodorder.util.FileUtil;

public class CartItemRepository {
    Connection connection;
    PreparedStatement preparedStatement;

    public CartItemRepository(){
        connection = DatabaseConnection
                        .getInstance()
                        .getConnection();
    }

    public void save(CartItem cartItem) {
        String query = "INSERT INTO cart_items (cart_id, menu_item_id, quantity) VALUES (?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, Long.parseLong(cartItem.getCartId()));
            preparedStatement.setLong(2, Long.parseLong(cartItem.getMenuItemId()));
            preparedStatement.setInt(3, cartItem.getQuantity());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void update(CartItem cartItem) {
        List<CartItem> cartItems = FileUtil.readData(FileConstants.CART_ITEMS_FILE);

        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getId().equals(cartItem.getId())) {
                cartItems.set(i, cartItem);

                FileUtil.writeData(FileConstants.CART_ITEMS_FILE, cartItems);
                return;
            }
        }

        throw new CartItemNotFoundException(MessageConstants.CART_ITEM_NOT_FOUND);
    }

    public CartItem findById(String id) {
        String query = "SELECT * FROM cart_items WHERE cart_item_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, Long.parseLong(id));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                CartItem cartItem = new CartItem();

                cartItem.setId(String.valueOf(resultSet.getLong("cart_item_id")));
                cartItem.setCartId(String.valueOf(resultSet.getLong("cart_id")));
                cartItem.setMenuItemId(String.valueOf(resultSet.getLong("menu_item_id")));
                cartItem.setQuantity(resultSet.getInt("quantity"));

                return cartItem;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }

    public List<CartItem> findByCartId(String cartId) {
        List<CartItem> cartItemsByCart = new ArrayList<>();

        String query = "SELECT * FROM cart_items WHERE cart_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, Long.parseLong(cartId));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CartItem cartItem = new CartItem();

                cartItem.setId(String.valueOf(resultSet.getLong("cart_item_id")));
                cartItem.setCartId(String.valueOf(resultSet.getLong("cart_id")));
                cartItem.setMenuItemId(String.valueOf(resultSet.getLong("menu_item_id")));
                cartItem.setQuantity(resultSet.getInt("quantity"));

                cartItemsByCart.add(cartItem);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return cartItemsByCart;
    }

    public void delete(String cartItemId) {
        String query = "DELETE FROM cart_items WHERE cart_item_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, Long.parseLong(cartItemId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteByCartId(String cartId) {
        String query = "DELETE FROM cart_items WHERE cart_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, Long.parseLong(cartId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<CartItem> findAll() {
        return FileUtil.readData(FileConstants.CART_ITEMS_FILE);
    }

}
