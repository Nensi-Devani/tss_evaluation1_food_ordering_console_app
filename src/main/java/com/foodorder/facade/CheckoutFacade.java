package com.foodorder.facade;

import com.foodorder.enums.PaymentType;
import com.foodorder.model.*;
import com.foodorder.service.*;

public class CheckoutFacade {
    private final UserService userService = new UserService();
    private final RestaurantService restaurantService = new RestaurantService();
    private final MenuItemService menuItemService = new MenuItemService();
    private final CartService cartService = new CartService();
    private final OrderService orderService = new OrderService();
    private final PaymentService paymentService = new PaymentService();

    // USER
    public void registerUser(User user) {
        userService.register(user);
    }

    public User login(String email, String password) {
        return userService.login(email, password);
    }

    // RESTAURANT
    public void registerRestaurant(Restaurant restaurant) {
        restaurantService.register(restaurant);
    }

    // MENU
    public void addMenuItem(MenuItem menuItem) {
        menuItemService.addMenuItem(menuItem);
    }

    // CART
    public Cart createCart(Cart cart) {
        return cartService.createCart(cart);
    }

    public void addToCart(CartItem cartItem) {
        cartService.addItem(cartItem);
    }

    public void updateCartItem(CartItem cartItem) {
        cartService.updateItem(cartItem);
    }

    public void removeCartItem(String cartItemId) {
        cartService.removeItem(cartItemId);
    }

    // ORDER
    public Order placeOrder(String customerId,  String deliveryAddressId, PaymentType paymentType, double deliveryCharge) {
        return orderService.placeOrder(
                customerId,
                deliveryAddressId,
                paymentType,
                deliveryCharge
        );
    }

    public void cancelOrder(String orderId, String customerId) {
        orderService.cancelOrder(orderId, customerId);
    }

    public void acceptOrder(String orderId, String restaurantOwnerId) {
        orderService.acceptOrder(orderId, restaurantOwnerId);
    }

    public void markOrderReady(String orderId, String restaurantOwnerId) {
        orderService.markOrderReady(orderId, restaurantOwnerId);
    }

    public void markOutForDelivery(String orderId, String restaurantOwnerId) {
        orderService.markOutForDelivery(orderId, restaurantOwnerId);
    }

    public void deliverOrder(String orderId, String otp, String deliveryBoyId) {
        orderService.deliverOrder(orderId, otp, deliveryBoyId);
    }

    // PAYMENT
    public Payment makePayment(Payment payment) {
        return paymentService.createPayment(payment);
    }

    public Cart getCart(String customerId) {
        return cartService.getCart(customerId);
    }

    public Payment getPayment(String orderId) {
        return paymentService.getPaymentByOrderId(orderId);
    }

    public void approveRestaurant(String restaurantId) {
        restaurantService.approveRestaurant(restaurantId);
    }

    public void approveUser(String userId) {
        userService.approveUser(userId);
    }

}