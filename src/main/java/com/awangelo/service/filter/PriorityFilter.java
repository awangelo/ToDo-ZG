package com.awangelo.service.filter;

import com.awangelo.model.Priority;
import com.awangelo.model.Task;

public class PriorityFilter implements TaskFilter {
    private final Priority priority;

    public PriorityFilter(Priority priority) {
        this.priority = priority;
    }

    @Override
    public boolean matches(Task task) {
        return task.getPriority().equals(priority);
    }
}
