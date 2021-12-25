package com.example.socialdance.utils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateTimeUtils {

    public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    static {
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
}
