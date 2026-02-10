package com.awangelo.service;

import com.awangelo.model.Priority;

public class AlarmUtils {
    public static int getAlarmMinutesBefore(Priority priority) {
        return switch (priority) {
            case VERY_HIGH -> 30;
            case HIGH -> 60;
            case MEDIUM -> 120;
            case LOW -> 240;
            case VERY_LOW -> 480;
        };
    }
}
