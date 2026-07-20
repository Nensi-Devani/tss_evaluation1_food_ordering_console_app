package com.foodorder.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
        List<CartItem> cartItems = FileUtil.readData(FileConstants.CART_ITEMS_FILE);

        for (CartItem cartItem : cartItems) {
            if (cartItem.getId().equals(id))
                return cartItem;
        }

        throw new CartItemNotFoundException(MessageConstants.CART_ITEM_NOT_FOUND);
    }

    public List<CartItem> findByCartId(String cartId) {
        List<CartItem> cartItemsByCart = new ArrayList<>();

        List<CartItem> cartItems = FileUtil.readData(FileConstants.CART_ITEMS_FILE);

        for (CartItem cartItem : cartItems) {
            if (cartItem.getCartId().equals(cartId))
                cartItemsByCart.add(cartItem);
        }

        return cartItemsByCart;
    }

    public List<CartItem> findAll() {
        return FileUtil.readData(FileConstants.CART_ITEMS_FILE);
    }

}
