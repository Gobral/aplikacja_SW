package com.example.myapplication;

import androidx.room.TypeConverter;

import java.util.Date;

public class MyTypeConverters {
    @TypeConverter
    public static Date dateFromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
