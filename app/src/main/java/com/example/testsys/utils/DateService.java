package com.example.testsys.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateService {
    static public String fromCalendar(Calendar calendar) {
        String str = calendar.get(Calendar.DAY_OF_MONTH) + "-";
        str += calendar.get(Calendar.MONTH) + "-";
        str += calendar.get(Calendar.YEAR);

        return str;
    }

    static public Calendar fromString(String date) {
        String[] split = date.split("-");
        int dayOfMonth = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int year = Integer.parseInt(split[2]);
        return new GregorianCalendar(year, month, dayOfMonth);
    }
}
