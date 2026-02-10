package com.awangelo.model;

import java.io.*;
import java.time.LocalDateTime;
import java.util.UUID;

public final class Task implements Serializable {
    private final UUID id;
    private String name;
    private String description;
    private transient LocalDateTime deadline;
    private Priority priority;
    private String category;
    private Status status;
    private boolean hasAlarm;
    private boolean alarmTriggered;

    public Task(String name, String description, LocalDateTime deadline, Priority priority, String category) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.category = category;
        this.status = Status.TODO;
        this.hasAlarm = false;
        this.alarmTriggered = false;
    }

    public Task(String name, String description, LocalDateTime deadline, Priority priority, String category, boolean hasAlarm) {
        this(name, description, deadline, priority, category);
        this.hasAlarm = hasAlarm;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(deadline.toString());
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        deadline = LocalDateTime.parse((String) in.readObject());
    }


    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean hasAlarm() {
        return hasAlarm;
    }

    public void setHasAlarm(boolean hasAlarm) {
        this.hasAlarm = hasAlarm;
    }

    public boolean isAlarmTriggered() {
        return alarmTriggered;
    }

    public void setAlarmTriggered(boolean alarmTriggered) {
        this.alarmTriggered = alarmTriggered;
    }
}