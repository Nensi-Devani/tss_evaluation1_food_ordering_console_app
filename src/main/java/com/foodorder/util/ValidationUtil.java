package com.foodorder.util;

import java.util.regex.Pattern;

public final class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern MOBILE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");

    public static boolean isValidEmail(String email) {
        return email != null && !email.trim().isEmpty() && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidMobile(String mobileNumber) {
        return mobileNumber != null && MOBILE_PATTERN.matcher(mobileNumber).matches();
    }

    public static boolean isNullOrBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}
