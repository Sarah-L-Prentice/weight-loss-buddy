package com.prenticeweb.weightlossbuddy.room.entity;

import androidx.room.TypeConverter;

import java.math.BigDecimal;
import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String fromBigDecimal(BigDecimal value) {
        return value.toString();
    }

    @TypeConverter
    public static BigDecimal stringToBigDecimal(String value) {
        return new BigDecimal(value);
    }
}
