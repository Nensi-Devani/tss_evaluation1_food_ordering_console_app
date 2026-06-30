package com.foodorder.repository;

import java.util.ArrayList;
import java.util.List;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.IdConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.exception.CartItemNotFoundException;
import com.foodorder.model.CartItem;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

public class CartItemRepository {

    public void save(CartItem cartItem) {
        List<CartItem> cartItems = FileUtil.readData(FileConstants.CART_ITEMS_FILE);

        cartItem.setId(IdGenerator.generateId(
                FileConstants.CART_ITEMS_FILE,
                IdConstants.CART_ITEM_ID_PREFIX));

        cartItems.add(cartItem);

        FileUtil.writeData(FileConstants.CART_ITEMS_FILE, cartItems);
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
