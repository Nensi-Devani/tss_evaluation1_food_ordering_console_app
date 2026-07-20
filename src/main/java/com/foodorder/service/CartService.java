package com.foodorder.service;

import java.util.List;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.exception.CartNotFoundException;
import com.foodorder.model.Cart;
import com.foodorder.model.CartItem;
import com.foodorder.repository.CartItemRepository;
import com.foodorder.repository.CartRepository;
import com.foodorder.util.FileUtil;

public class CartService {
    private final CartRepository cartRepository = new CartRepository();
    private final CartItemRepository cartItemRepository = new CartItemRepository();

    // Create cart (reuse if same customer + same restaurant)
    public Cart createCart(Cart cart) {
        Cart existingCart = cartRepository.findByCustomerId(cart.getCustomerId());

        if (existingCart != null && existingCart.getRestaurantId().equals(cart.getRestaurantId()))
            return existingCart;

        cartRepository.save(cart);
        return cart;
    }

    // Add item to cart
    public void addItem(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    // Update item quantity
    public void updateItem(CartItem cartItem) {
        cartItemRepository.update(cartItem);
    }

    // Remove item from cart
    public void removeItem(String cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId);

        if (cartItem == null) {
            throw new CartNotFoundException(MessageConstants.CART_NOT_FOUND);
        }

        cartItemRepository.delete(cartItemId);
    }

    // Get cart by customerId
    public Cart getCart(String customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId);

        if (cart == null)
            throw new CartNotFoundException(MessageConstants.CART_NOT_FOUND);

        return cart;
    }

    // Get cart items
    public List<CartItem> getCartItems(String cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    // Clear cart after order placed
    public void clearCart(String cartId) {
        List<CartItem> cartItems = cartItemRepository.findAll();

        cartItems.removeIf(item -> item.getCartId().equals(cartId));

        FileUtil.writeData(FileConstants.CART_ITEMS_FILE, cartItems);
    }
}