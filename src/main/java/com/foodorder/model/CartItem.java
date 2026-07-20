package com.foodorder.model;

import com.foodorder.common.Identifiable;

import java.io.Serializable;

public class CartItem implements Serializable, Identifiable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String cartId;
    private String menuItemId;
    private int quantity;

    public CartItem(){
    }

    public CartItem(String cartId, String menuItemId, int quantity) {
        this.cartId = cartId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
