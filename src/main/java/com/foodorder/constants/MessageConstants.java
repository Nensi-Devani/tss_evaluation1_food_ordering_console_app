package com.foodorder.constants;

public class MessageConstants {
    // Authentication
    public static final String INVALID_EMAIL_OR_PASSWORD = "Invalid email or password.";
    public static final String ACCESS_DENIED = "You are not authorized to perform this operation.";
    public static final String INVALID_CREDENTIALS = "Invalid Credentials";

    // User
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists.";
    public static final String USER_INACTIVE = "User is inactive.";
    public static final String USER_ADDRESS_NOT_FOUND = "User address not found.";

    // Restaurant
    public static final String RESTAURANT_NOT_FOUND = "Restaurant not found.";
    public static final String RESTAURANT_INACTIVE = "Restaurant is inactive.";

    // Menu Item
    public static final String MENU_ITEM_NOT_FOUND = "Menu item not found.";
    public static final String MENU_ITEM_INACTIVE = "Menu item is inactive.";

    // Cart
    public static final String CART_NOT_FOUND = "Cart not found.";
    public static final String CART_EMPTY = "Cart is empty.";
    public static final String DIFFERENT_RESTAURANT_CART = "Cart can contain items from only one restaurant.";
    public static final String CART_ITEM_NOT_FOUND = "Cart item not found.";

    // Order
    public static final String ORDER_NOT_FOUND = "Order not found.";
    public static final String ORDER_CANNOT_CANCEL = "Order cannot be cancelled after it is ready for delivery.";
    public static final String INVALID_ORDER_STATE = "Invalid order state.";
    public static final String ORDER_ITEM_NOT_FOUND = "Order item not found.";

    // Payment
    public static final String PAYMENT_FAILED = "Payment failed.";

    // Discount
    public static final String DISCOUNT_NOT_FOUND = "Discount not found.";

    // Delivery
    public static final String DELIVERY_BOY_NOT_AVAILABLE = "No delivery partner is available.";
    public static final String DELIVERY_BOY_CAPACITY_EXCEEDED = "Delivery partner has reached maximum order capacity.";

    // OTP
    public static final String OTP_NOT_FOUND = "OTP not found.";
    public static final String OTP_MISMATCH = "Invalid OTP.";

    // OrderLog
    public static final String ORDER_LOG_NOT_FOUND = "OrderLog not found.";

    // Validation
    public static final String INVALID_INPUT = "Invalid input.";
    public static final String REQUIRED_FIELDS_MISSING = "Required fields are missing.";
}
