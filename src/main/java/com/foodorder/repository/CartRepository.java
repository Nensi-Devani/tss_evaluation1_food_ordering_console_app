package com.foodorder.repository;

import java.util.List;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.IdConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.exception.CartNotFoundException;
import com.foodorder.model.Cart;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

public class CartRepository {
    public void save(Cart cart) {
        List<Cart> carts = FileUtil.readData(FileConstants.CARTS_FILE);

        cart.setId(IdGenerator.generateId(
                FileConstants.CARTS_FILE,
                IdConstants.CART_ID_PREFIX));

        carts.add(cart);

        FileUtil.writeData(FileConstants.CARTS_FILE, carts);
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
        List<Cart> carts = FileUtil.readData(FileConstants.CARTS_FILE);

        for (Cart cart : carts) {
            if (cart.getCustomerId().equals(customerId))
                return cart;
        }

        return null;
    }

    public List<Cart> findAll() {
        return FileUtil.readData(FileConstants.CARTS_FILE);
    }
}
