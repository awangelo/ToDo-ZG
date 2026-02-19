package com.awangelo.service.filter;

import com.awangelo.model.Task;

@FunctionalInterface
public interface TaskFilter {
    boolean matches(Task task);

    default TaskFilter and(TaskFilter other) {
        return task -> this.matches(task) && other.matches(task);
    }

    default TaskFilter or(TaskFilter other) {
        return task -> this.matches(task) || other.matches(task);
    }

    default TaskFilter negate() {
        return task -> !this.matches(task);
    }
}
