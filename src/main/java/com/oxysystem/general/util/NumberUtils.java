package com.oxysystem.general.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.text.DecimalFormat;

@Component
public class NumberUtils {
    private final SecureRandom secureRandom = new SecureRandom();
    public static String formatNumber(double number, String pattern){
        if (pattern == null){
            pattern = "#,##0.00";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(number);
    }
    public static String formatNumber(double number){
        return formatNumber(number, "#,##0.00");
    }
    public int generateRandomNumber(int digits) {
        int min = (int) Math.pow(10, digits - 1);
        int max = (int) Math.pow(10, digits) - 1;
        return secureRandom.nextInt((max - min) + 1) + min;
    }

    public static BigDecimal numberScale(BigDecimal bigDecimal, int newScale, RoundingMode roundingMode){
        return bigDecimal.setScale(newScale,roundingMode);
    }

    public static BigDecimal numberScale(BigDecimal bigDecimal){
        return numberScale(bigDecimal,2, RoundingMode.HALF_UP);
    }
}
