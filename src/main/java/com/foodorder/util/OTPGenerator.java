package com.foodorder.util;

import com.foodorder.constants.AppConstants;

import java.util.Random;

public final class OTPGenerator {
    private static final Random RANDOM = new Random();

    public static String generateOTP() {
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < AppConstants.OTP_LENGTH; i++) {
            otp.append(RANDOM.nextInt(10));
        }

        return otp.toString();
    }
}
