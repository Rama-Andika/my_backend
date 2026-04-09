package com.oxysystem.general.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtils {
    public static String formatDate(LocalDateTime date, String format){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return date.format(dateTimeFormatter);
    }
}
