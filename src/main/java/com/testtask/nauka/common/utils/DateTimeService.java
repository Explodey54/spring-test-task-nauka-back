package com.testtask.nauka.common.utils;

import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Service
public class DateTimeService {

    private final String DATE_FORMAT = "yyyy-MM-dd";
    private final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    public Calendar getTodayCalendar() {
        return new GregorianCalendar();
    }

    public Calendar getCalendarFromString(String input) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateFormat.parse(input));
        return cal;
    }

}
