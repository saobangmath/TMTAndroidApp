package com.example.dotgeneration.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtils {
    public static String convert(String datetime){
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateFormat sgtFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        sgtFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
        try {
            Date utcDate = utcFormat.parse(datetime);
            String sgtString = sgtFormat.format(utcDate);
            return sgtString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
