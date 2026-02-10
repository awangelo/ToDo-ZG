package com.awangelo.service;

import com.awangelo.model.Priority;
import com.awangelo.model.Status;
import com.awangelo.model.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public final class TaskService implements ITaskService {
    private final List<Task> tasks = new ArrayList<>();
    // Tasks are sorted by priority (HIGH to LOW) and then by deadline (earliest first)
    private final Comparator<Task> taskComparator = Comparator
            .comparing(Task::getPriority, Comparator.reverseOrder())
            .thenComparing(Task::getDeadline);

    // Create a new task and insert it in the correct position
    public void create(Task task) {
        int position = Collections.binarySearch(tasks, task, taskComparator);
        if (position < 0) {
            position = -(position + 1);
        }
        tasks.add(position, task);
    }

    public boolean delete(UUID id) {
        Task task = findById(id);
        if (task != null) {
            tasks.remove(task);
            return true;
        }
        return false;
    }

    public boolean update(UUID id, String name, String description, LocalDateTime deadline,
                          Priority priority, String category, Status status) {
        Task task = findById(id);
        if (task == null) {
            return false;
        }

        tasks.remove(task);

        task.setName(name);
        task.setDescription(description);
        task.setDeadline(deadline);
        task.setPriority(priority);
        task.setCategory(category);
        task.setStatus(status);

        int position = Collections.binarySearch(tasks, task, taskComparator);
        if (position < 0) {
            position = -(position + 1);
        }
        tasks.add(position, task);

        return true;
    }

    public Task findById(UUID id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Task> listAll() {
        return new ArrayList<>(tasks);
    }

    public List<Task> listByCategory(String category) {
        return tasks.stream()
                .filter(task -> task.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<Task> listByPriority(Priority priority) {
        return tasks.stream()
                .filter(task -> task.getPriority().equals(priority))
                .collect(Collectors.toList());
    }

    public List<Task> listByStatus(Status status) {
        return tasks.stream()
                .filter(task -> task.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public List<Task> listByDateRange(LocalDateTime start, LocalDateTime end) {
        return tasks.stream()
                .filter(task -> !task.getDeadline().isBefore(start) && !task.getDeadline().isAfter(end))
                .collect(Collectors.toList());
    }

    public Map<Status, Long> countByStatus() {
        Map<Status, Long> counts = new HashMap<>();
        for (Status status : Status.values()) {
            long count = tasks.stream()
                    .filter(task -> task.getStatus().equals(status))
                    .count();
            counts.put(status, count);
        }
        return counts;
    }
}