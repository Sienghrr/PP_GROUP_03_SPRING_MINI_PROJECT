package com.kshrd.habittracker.utils;

import java.util.concurrent.ThreadLocalRandom;

public final class OtpUtil {

    private OtpUtil() {
    }

    public static String generateSixDigitOtp() {
        int otp = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.valueOf(otp);
    }
}