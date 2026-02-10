import {loadTasks} from "./storage.js";

export let currentEditingId = null;

export function getFormData() {
    return {
        name: document.getElementById('task-name').value,
        description: document.getElementById('task-description').value || '', // opcional
        deadline: document.getElementById('task-deadline').value || null, // opcional
        priority: parseInt(document.getElementById('task-priority').value) || 3,
        status: document.getElementById('task-status').value,
        hasAlarm: document.getElementById('task-alarm').checked
    };
}

export function clearForm() {
    document.getElementById('task-form').reset();
    document.getElementById('task-priority').value = 3;
    document.getElementById('form-title').textContent = 'Nova Tarefa';
    currentEditingId = null;
}

export function fillFormForEdit(id) {
    const task = loadTasks().find(t => t.id === id);
    if (!task) {
        alert('Tarefa nao encontrada!');
        return;
    }

    document.getElementById('task-name').value = task.name;
    document.getElementById('task-description').value = task.description || '';
    document.getElementById('task-deadline').value = task.deadline || '';
    document.getElementById('task-priority').value = task.priority;
    document.getElementById('task-status').value = task.status;
    document.getElementById('task-alarm').checked = task.hasAlarm;
    document.getElementById('form-title').textContent = 'Editar Tarefa #' + task.id;
    currentEditingId = task.id;
}
