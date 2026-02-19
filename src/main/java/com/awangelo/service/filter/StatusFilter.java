package com.awangelo.service.filter;

import com.awangelo.model.Status;
import com.awangelo.model.Task;

public class StatusFilter implements TaskFilter {
    private final Status status;

    public StatusFilter(Status status) {
        this.status = status;
    }

    @Override
    public boolean matches(Task task) {
        return task.getStatus().equals(status);
    }
}
