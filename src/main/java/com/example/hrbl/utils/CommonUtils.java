package com.example.hrbl.utils;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CommonUtils {

    public LocalDateTime getLocalDateTimeFromString(String dateStr, String timeStr) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime ldt = LocalDateTime.parse(dateStr + " " + timeStr, formatter);
        return ldt;
    }

}
