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

        // RESTAURANT
        public void registerRestaurant(Restaurant restaurant) {
            restaurantService.register(restaurant);
        }

        // MENU
        public void addMenuItem(MenuItem item) {
            menuItemService.addMenuItem(item);
        }

        // CART
        public void addToCart(CartItem item) {
            cartService.addItem(item);
        }

        // ORDER
        public Order placeOrder(String customerId, PaymentType paymentType, double deliveryCharge) {
            return orderService.placeOrder(customerId, paymentType, deliveryCharge);
        }

        // PAYMENT
        public Payment makePayment(Payment payment) {
            return paymentService.createPayment(payment);
        }
}
