package com.awangelo.service;

import com.awangelo.model.Status;
import com.awangelo.model.Task;
import com.awangelo.model.Priority;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AlarmService implements IAlarmService {
    private final ITaskService taskService;
    private final ScheduledExecutorService executor;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public AlarmService(ITaskService taskService) {
        this.taskService = taskService;
        this.executor = Executors.newScheduledThreadPool(1);
    }

    public void start() {
        executor.scheduleAtFixedRate(this::checkAlarms, 0, 1, TimeUnit.MINUTES);
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void checkAlarms() {
        LocalDateTime now = LocalDateTime.now();

        for (Task task : taskService.listAll()) {
            if (shouldTriggerAlarm(task, now)) {
                triggerAlarm(task);
                task.setAlarmTriggered(true);
            }
        }
    }

    private boolean shouldTriggerAlarm(Task task, LocalDateTime now) {
        if (!task.hasAlarm()) {
            return false;
        }

        if (task.isAlarmTriggered()) {
            return false;
        }

        if (task.getStatus() != Status.TODO) {
            return false;
        }

        LocalDateTime alarmTime = task.getDeadline().minusMinutes(AlarmUtils.getAlarmMinutesBefore(task.getPriority()));
        return !now.isBefore(alarmTime);
    }

    private void triggerAlarm(Task task) {
        System.out.println("BEEP! BEEP! BEEP!");

        long minutesUntilDeadline = java.time.Duration.between(LocalDateTime.now(), task.getDeadline()).toMinutes();

        System.out.println();
        System.out.println("!!!!!!!!!!!!!!");
        System.out.println("!!  ALARME  !!");
        System.out.println("!!!!!!!!!!!!!!");
        System.out.printf("Tarefa: %s\n", task.getName());
        System.out.printf("Prazo: %s\n", task.getDeadline().format(dateFormatter));
        System.out.printf("Tempo restante: %s\n", formatTimeRemaining(minutesUntilDeadline));
        System.out.printf("Prioridade: %s\n", task.getPriority());
        System.out.println();
    }

    private String formatTimeRemaining(long minutes) {
        if (minutes < 0) {
            return "PRAZO EXPIRADO!";
        }
        if (minutes == 0) {
            return "Menos de 1 minuto";
        }
        if (minutes < 60) {
            return minutes + " minuto(s)";
        }

        long hours = minutes / 60;
        long remainingMinutes = minutes % 60;

        if (remainingMinutes == 0) {
            return hours + " hora(s)";
        }
        return hours + " hora(s) e " + remainingMinutes + " minuto(s)";
    }
}
