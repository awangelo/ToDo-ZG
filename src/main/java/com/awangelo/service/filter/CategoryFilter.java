package com.awangelo.service.filter;

import com.awangelo.model.Task;

public class CategoryFilter implements TaskFilter {
    private final String category;

    public CategoryFilter(String category) {
        this.category = category;
    }

    @Override
    public boolean matches(Task task) {
        return task.getCategory().equalsIgnoreCase(category);
    }
}
