package io.mattcarroll.androidtesting.overview;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

class Time {

    public boolean isToday(long date) {
        Calendar today = Calendar.getInstance();
        Calendar otherDay = Calendar.getInstance();
        otherDay.setTime(new Date(date));

        return isSameYear(today, otherDay) && isSameDayOfYear(today, otherDay);
    }

    public boolean isYesterday(long date) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);

        Calendar otherDay = Calendar.getInstance();
        otherDay.setTime(new Date(date));

        return isSameYear(yesterday, otherDay) && isSameDayOfYear(yesterday, otherDay);
    }

    public boolean isOnSameDay(long date1, long date2) {
        Calendar day1 = Calendar.getInstance();
        Calendar day2 = Calendar.getInstance();
        day1.setTime(new Date(date1));
        day2.setTime(new Date(date2));
        return day1.get(Calendar.YEAR) == day2.get(Calendar.YEAR) &&
                day1.get(Calendar.DAY_OF_YEAR) == day2.get(Calendar.DAY_OF_YEAR);
    }

    private boolean isSameYear(@NonNull Calendar day1, @NonNull Calendar day2) {
        return day1.get(Calendar.YEAR) == day2.get(Calendar.YEAR);
    }

    private boolean isSameDayOfYear(@NonNull Calendar day1, @NonNull Calendar day2) {
        return day1.get(Calendar.DAY_OF_YEAR) == day2.get(Calendar.DAY_OF_YEAR);
    }

    public boolean isThisWeek(long date) {
        Calendar today = Calendar.getInstance();
        Calendar otherDay = Calendar.getInstance();
        otherDay.setTime(new Date(date));

        return isSameYear(today, otherDay) && isSameWeekOfYear(today, otherDay);
    }

    private boolean isSameWeekOfYear(@NonNull Calendar day1, @NonNull Calendar day2) {
        return day1.get(Calendar.WEEK_OF_YEAR) == day2.get(Calendar.WEEK_OF_YEAR);
    }

}
