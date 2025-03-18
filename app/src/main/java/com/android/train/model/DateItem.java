package com.android.train.model;

public class DateItem {
    private final String dayOfWeek;
    private final String date;

    public DateItem(String dayOfWeek, String date) {
        this.dayOfWeek = dayOfWeek;
        this.date = date;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getDate() {
        return date;
    }
}
