package com.oxysystem.general.util;

import org.springframework.stereotype.Component;

@Component
public class StringUtils {
    public static String formatCounter(int digit, int counter){
        String formatCounter = String.format("%0"+digit+"d",counter);
        return formatCounter;
    }
}
