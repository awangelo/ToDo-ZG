package com.awangelo.service;

import com.awangelo.model.Priority;
import com.awangelo.model.Status;
import com.awangelo.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @AfterEach
    void tearDown() {
        System.out.println("=============== OK ===============\n");
    }

    @Test
    @DisplayName("Deve criar uma tarefa com sucesso")
    void create_DeveAdicionarTarefaNaLista() {
        System.out.println("\n=== CREATE: Adicionar tarefa ===");
        // GIVEN: Uma tarefa valida
        String taskName = "Comprar leite";
        Task task = new Task(
                taskName,
                "Ir ao mercado comprar leite",
                LocalDateTime.now().plusDays(1),
                Priority.HIGH,
                "Compras"
        );
        int initialSize = taskService.listAll().size();

        // WHEN: Usuario tenta criar a tarefa
        taskService.create(task);

        // THEN: A tarefa deve ser adicionada na lista
        assertEquals(initialSize + 1, taskService.listAll().size());
        assertEquals(taskName, taskService.listAll().getFirst().getName());
        System.out.println("Tarefa '" + taskName + "' criada com sucesso");
    }

    @Test
    @DisplayName("Deve ordenar tarefas por prioridade ao criar")
    void create_DeveOrdenarPorPrioridade() {
        System.out.println("\n=== CREATE: Ordenar por prioridade ===");
        // GIVEN: 3 tarefas com prioridades diferentes
        LocalDateTime deadline = LocalDateTime.now().plusDays(1);

        Task taskLow = new Task("Tarefa Low", "Desc", deadline, Priority.LOW, "Geral");
        Task taskHigh = new Task("Tarefa High", "Desc", deadline, Priority.HIGH, "Geral");
        Task taskMedium = new Task("Tarefa Medium", "Desc", deadline, Priority.MEDIUM, "Geral");

        // WHEN: Sao criadas tarefas em qualquer ordem
        taskService.create(taskLow);
        taskService.create(taskHigh);
        taskService.create(taskMedium);

        // THEN: Devem estar ordenadas HIGH > MEDIUM > LOW
        assertEquals("Tarefa High", taskService.listAll().get(0).getName());
        assertEquals("Tarefa Medium", taskService.listAll().get(1).getName());
        assertEquals("Tarefa Low", taskService.listAll().get(2).getName());
        System.out.println("Tarefas ordenadas: HIGH > MEDIUM > LOW");
    }

    @Test
    @DisplayName("Deve deletar uma tarefa existente")
    void delete_DeveRemoverTarefaExistente() {
        System.out.println("\n=== DELETE: Remover tarefa existente ===");
        // GIVEN: Uma tarefa criada
        Task task = new Task("Tarefa para deletar", "Desc",
                LocalDateTime.now().plusDays(1), Priority.MEDIUM, "Geral");
        taskService.create(task);
        assertEquals(1, taskService.listAll().size());
        int taskCountBeforeDeletion = taskService.listAll().size();

        // WHEN: A tarefa for deletada pelo ID
        boolean result = taskService.delete(task.getId());

        // THEN: Tarefa deve ser removida
        assertTrue(result);
        assertEquals(taskCountBeforeDeletion - 1, taskService.listAll().size());
        System.out.println("Tarefa '" + task.getName() + "' removida com sucesso");
    }

    @Test
    @DisplayName("Deve retornar false ao deletar tarefa inexistente")
    void delete_DeveRetornarFalseParaTarefaInexistente() {
        System.out.println("\n=== DELETE: Tarefa inexistente ===");
        // GIVEN: Nenhuma tarefa criada

        // WHEN: Tenta deletar com ID inexistente
        boolean result = taskService.delete(java.util.UUID.randomUUID());

        // THEN - Deve falhar
        assertFalse(result);
        System.out.println("Retornou false para ID inexistente");
    }

    @Test
    @DisplayName("Deve atualizar uma tarefa existente")
    void update_DeveAtualizarTarefaExistente() {
        System.out.println("\n=== UPDATE: Atualizar tarefa existente ===");
        // GIVEN: Uma tarefa criada
        Task task = new Task("Nome Antigo", "Descricao antiga",
                LocalDateTime.now().plusDays(1), Priority.LOW, "Categoria antiga");
        taskService.create(task);

        // WHEN: Atualiza a tarefa
        LocalDateTime newDeadline = LocalDateTime.now().plusDays(2);
        boolean result = taskService.update(
                task.getId(),
                "Nome Novo",
                "Descricao nova",
                newDeadline,
                Priority.HIGH,
                "Categoria nova",
                Status.DOING
        );

        // THEN - Tarefa deve estar atualizada
        assertTrue(result);
        Task updatedTask = taskService.findById(task.getId());
        assertEquals("Nome Novo", updatedTask.getName());
        assertEquals("Descricao nova", updatedTask.getDescription());
        assertEquals(Priority.HIGH, updatedTask.getPriority());
        assertEquals(Status.DOING, updatedTask.getStatus());
        System.out.println("Tarefa atualizada: " + updatedTask.getName() + " - Status: " + updatedTask.getStatus());
    }

    @Test
    @DisplayName("Deve retornar false ao atualizar tarefa inexistente")
    void update_DeveRetornarFalseParaTarefaInexistente() {
        System.out.println("\n=== UPDATE: Tarefa inexistente ===");
        // GIVEN: Lista vazia

        // WHEN: Tenta atualizar
        boolean result = taskService.update(
                java.util.UUID.randomUUID(),
                "Nome", "Desc", LocalDateTime.now(),
                Priority.LOW, "Cat", Status.TODO
        );

        // THEN: Deve retornar false
        assertFalse(result);
        System.out.println("Retornou false para ID inexistente");
    }

    @Test
    @DisplayName("Deve encontrar tarefa por ID")
    void findById_DeveEncontrarTarefaExistente() {
        System.out.println("\n=== FIND: Buscar tarefa por ID ===");
        // GIVEN: Uma tarefa criada
        Task task = new Task("Minha Tarefa", "Descricao",
                LocalDateTime.now().plusDays(1), Priority.MEDIUM, "Geral");
        taskService.create(task);

        // WHEN: Busca por ID
        Task found = taskService.findById(task.getId());

        // THEN: Deve retornar a tarefa correta
        assertNotNull(found);
        assertEquals(task.getId(), found.getId());
        assertEquals("Minha Tarefa", found.getName());
        System.out.println("Tarefa encontrada: " + found.getName() + " - ID: " + found.getId());
    }

    @Test
    @DisplayName("Deve retornar null para ID inexistente")
    void findById_DeveRetornarNullParaIdInexistente() {
        System.out.println("\n=== FIND: ID inexistente ===");
        // GIVEN: Lista vazia

        // WHEN: Busca ID aleatorio
        Task found = taskService.findById(java.util.UUID.randomUUID());

        // THEN: Deve retornar null
        assertNull(found);
        System.out.println("Retornou null para ID inexistente");
    }
}