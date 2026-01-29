package com.awangelo;

import com.awangelo.model.Priority;
import com.awangelo.model.Status;
import com.awangelo.model.Task;
import com.awangelo.service.PersistenceService;
import com.awangelo.service.TaskService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    private static final TaskService taskService = new TaskService();
    private static final PersistenceService persistenceService = new PersistenceService();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    static void main() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createTask();
                    break;
                case "2":
                    listAllTasks();
                    break;
                case "3":
                    listByCategory();
                    break;
                case "4":
                    listByPriority();
                    break;
                case "5":
                    listByStatus();
                    break;
                case "6":
                    listByDateRange();
                    break;
                case "7":
                    updateTask();
                    break;
                case "8":
                    deleteTask();
                    break;
                case "9":
                    showStatistics();
                    break;
                case "10":
                    saveTasks();
                    break;
                case "11":
                    loadTasks();
                    break;
                case "0":
                    running = false;
                    System.out.println("Encerrando aplicação...");
                    break;
                default:
                    System.out.println("Opção inválida.\n");
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("=== Menu ===");
        System.out.println("1.  Criar tarefa");
        System.out.println("2.  Listar todas as tarefas");
        System.out.println("3.  Listar por categoria");
        System.out.println("4.  Listar por prioridade");
        System.out.println("5.  Listar por status");
        System.out.println("6.  Listar por intervalo de data");
        System.out.println("7.  Atualizar tarefa");
        System.out.println("8.  Deletar tarefa");
        System.out.println("9.  Estatísticas");
        System.out.println("10. Salvar tarefas");
        System.out.println("11. Carregar tarefas");
        System.out.println("0.  Sair");
        System.out.print("Escolha: ");
    }

    private static void createTask() {
        System.out.println("\n=== Criar Nova Tarefa ===");

        System.out.print("Nome: ");
        String name = scanner.nextLine();

        System.out.print("Descrição: ");
        String description = scanner.nextLine();

        LocalDateTime deadline = readDateTime("Data de término (yyyy-MM-dd HH:mm): ");
        if (deadline == null) return;

        Priority priority = readPriority();
        if (priority == null) return;

        System.out.print("Categoria: ");
        String category = scanner.nextLine();

        Task task = new Task(name, description, deadline, priority, category);
        taskService.create(task);

        System.out.println("Tarefa criada com sucesso! ID: " + task.getId() + "\n");
    }

    private static void listAllTasks() {
        System.out.println("\n=== Todas as Tarefas ===");
        List<Task> tasks = taskService.listAll();
        displayTasks(tasks);
    }

    private static void listByCategory() {
        System.out.print("\nCategoria: ");
        String category = scanner.nextLine();

        System.out.println("\n=== Tarefas da Categoria: " + category + " ===");
        List<Task> tasks = taskService.listByCategory(category);
        displayTasks(tasks);
    }

    private static void listByPriority() {
        Priority priority = readPriority();
        if (priority == null) return;

        System.out.println("\n=== Tarefas com Prioridade: " + priority + " ===");
        List<Task> tasks = taskService.listByPriority(priority);
        displayTasks(tasks);
    }

    private static void listByStatus() {
        Status status = readStatus();
        if (status == null) return;

        System.out.println("\n=== Tarefas com Status: " + status + " ===");
        List<Task> tasks = taskService.listByStatus(status);
        displayTasks(tasks);
    }

    private static void listByDateRange() {
        System.out.println("\n=== Filtrar por Intervalo de Data ===");

        LocalDateTime start = readDateTime("Data inicial (yyyy-MM-dd HH:mm): ");
        if (start == null) return;

        LocalDateTime end = readDateTime("Data final (yyyy-MM-dd HH:mm): ");
        if (end == null) return;

        System.out.println("\n=== Tarefas entre " + start.format(dateFormatter) + " e " + end.format(dateFormatter) + " ===");
        List<Task> tasks = taskService.listByDateRange(start, end);
        displayTasks(tasks);
    }

    private static void updateTask() {
        System.out.println("\n=== Atualizar Tarefa ===");

        System.out.print("ID da tarefa: ");
        UUID id = readUUID();
        if (id == null) return;

        Task existing = taskService.findById(id);
        if (existing == null) {
            System.out.println("Tarefa não encontrada!\n");
            return;
        }

        System.out.println("Tarefa atual: " + formatTask(existing));
        System.out.println("Digite os novos valores (Enter para manter o atual):\n");

        System.out.print("Nome [" + existing.getName() + "]: ");
        String name = scanner.nextLine();
        if (name.isEmpty()) name = existing.getName();

        System.out.print("Descrição [" + existing.getDescription() + "]: ");
        String description = scanner.nextLine();
        if (description.isEmpty()) description = existing.getDescription();

        System.out.print("Data de término [" + existing.getDeadline().format(dateFormatter) + "] (yyyy-MM-dd HH:mm): ");
        String deadlineStr = scanner.nextLine();
        LocalDateTime deadline = existing.getDeadline();
        if (!deadlineStr.isEmpty()) {
            try {
                deadline = LocalDateTime.parse(deadlineStr, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida! Mantendo valor atual.\n");
            }
        }

        System.out.print("Prioridade [" + existing.getPriority().getValue() + "] (1-5): ");
        String priorityStr = scanner.nextLine();
        Priority priority = existing.getPriority();
        if (!priorityStr.isEmpty()) {
            try {
                priority = Priority.fromValue(Integer.parseInt(priorityStr));
            } catch (Exception e) {
                System.out.println("Prioridade inválida! Mantendo valor atual.\n");
            }
        }

        System.out.print("Categoria [" + existing.getCategory() + "]: ");
        String category = scanner.nextLine();
        if (category.isEmpty()) category = existing.getCategory();

        System.out.print("Status [" + existing.getStatus() + "] (TODO/DOING/DONE): ");
        String statusStr = scanner.nextLine();
        Status status = existing.getStatus();
        if (!statusStr.isEmpty()) {
            try {
                status = Status.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Status inválido! Mantendo valor atual.\n");
            }
        }

        boolean updated = taskService.update(id, name, description, deadline, priority, category, status);
        if (updated) {
            System.out.println("Tarefa atualizada com sucesso!\n");
        } else {
            System.out.println("Erro ao atualizar tarefa!\n");
        }
    }

    private static void deleteTask() {
        System.out.println("\n=== Deletar Tarefa ===");

        System.out.print("ID da tarefa: ");
        UUID id = readUUID();
        if (id == null) return;

        boolean deleted = taskService.delete(id);
        if (deleted) {
            System.out.println("Tarefa deletada com sucesso!\n");
        } else {
            System.out.println("Tarefa não encontrada!\n");
        }
    }

    private static void showStatistics() {
        System.out.println("\n=== Estatísticas ===");
        Map<Status, Long> stats = taskService.countByStatus();

        for (Status status : Status.values()) {
            System.out.println(status + ": " + stats.getOrDefault(status, 0L));
        }
        System.out.println();
    }

    private static void saveTasks() {
        try {
            persistenceService.save(taskService.listAll());
            System.out.println("Tarefas salvas com sucesso!\n");
        } catch (Exception e) {
            System.out.println("Erro ao salvar tarefas: " + e.getMessage() + "\n");
        }
    }

    private static void loadTasks() {
        try {
            List<Task> loadedTasks = persistenceService.load();

            for (Task task : loadedTasks) {
                taskService.create(task);
            }

            System.out.println("Tarefas carregadas com sucesso! Total: " + loadedTasks.size() + "\n");
        } catch (Exception e) {
            System.out.println("Erro ao carregar tarefas: " + e.getMessage() + "\n");
        }
    }

    private static void displayTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada.\n");
            return;
        }

        for (Task task : tasks) {
            System.out.println(formatTask(task));
        }
        System.out.println();
    }

    private static String formatTask(Task task) {
        return String.format("[%s] %s | Prioridade: %d | Status: %s | Categoria: %s | Prazo: %s | Descrição: %s",
                task.getId().toString().substring(0, 8),
                task.getName(),
                task.getPriority().getValue(),
                task.getStatus(),
                task.getCategory(),
                task.getDeadline().format(dateFormatter),
                task.getDescription());
    }

    private static LocalDateTime readDateTime(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        try {
            return LocalDateTime.parse(input, dateFormatter);
        } catch (DateTimeParseException e) {
            System.out.println("Data inválida! Use o formato yyyy-MM-dd HH:mm\n");
            return null;
        }
    }

    private static Priority readPriority() {
        System.out.print("Prioridade (1-5): ");
        String input = scanner.nextLine();
        try {
            return Priority.fromValue(Integer.parseInt(input));
        } catch (Exception e) {
            System.out.println("Prioridade inválida! Use valores de 1 a 5.\n");
            return null;
        }
    }

    private static Status readStatus() {
        System.out.print("Status (TODO/DOING/DONE): ");
        String input = scanner.nextLine();
        try {
            return Status.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Status inválido!\n");
            return null;
        }
    }

    private static UUID readUUID() {
        String input = scanner.nextLine();
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            System.out.println("ID inválido!\n");
            return null;
        }
    }
}