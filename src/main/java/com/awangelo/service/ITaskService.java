package com.awangelo.service;

import com.awangelo.model.Priority;
import com.awangelo.model.Status;
import com.awangelo.model.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ITaskService {
    void create(Task task);

    boolean delete(UUID id);

    boolean update(UUID id, String name, String description, LocalDateTime deadline,
                   Priority priority, String category, Status status);

    Task findById(UUID id);

    List<Task> listAll();

    List<Task> listByCategory(String category);

    List<Task> listByPriority(Priority priority);

    List<Task> listByStatus(Status status);

    List<Task> listByDateRange(LocalDateTime start, LocalDateTime end);

    Map<Status, Long> countByStatus();
}
