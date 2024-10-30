package com.jaboumal.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static String dateToString(LocalDate date) {
        return date.format(FORMATTER);
    }

    public static LocalDate stringToDate(String date) {
        return LocalDate.parse(date, FORMATTER);
    }
}
