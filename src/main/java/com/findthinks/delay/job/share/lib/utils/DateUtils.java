package com.findthinks.delay.job.share.lib.utils;

import java.util.Calendar;

public final class DateUtils {

    public static Long getEndDayOfCurrentWeek() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.set(year, month, week, 0, 0, 0);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime().getTime() / 1000;
    }
}