package com.kafkaquest.kq.common.util.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DateTimeUtil {

    public static Long dateTimeToEpoch(LocalDateTime dateTime) {
        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static LocalDateTime epochToDateTime(Long longValue) {
        Instant instant = Instant.ofEpochMilli(longValue);
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
