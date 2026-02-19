package com.awangelo.service.filter;

import com.awangelo.model.Task;

import java.time.LocalDateTime;

public class DateRangeFilter implements TaskFilter {
    private final LocalDateTime start;
    private final LocalDateTime end;

    public DateRangeFilter(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean matches(Task task) {
        return !task.getDeadline().isBefore(start) && !task.getDeadline().isAfter(end);
    }
}
