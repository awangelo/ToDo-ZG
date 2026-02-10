import {getFilteredTasks, sortTasks} from "./taskService.js";

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

export function renderTasks() {
    const tasks = sortTasks(getFilteredTasks());
    const tbody = document.getElementById('tasks-body');
    const noMsg = document.getElementById('no-tasks-message');

    tbody.innerHTML = '';
    noMsg.style.display = tasks.length === 0 ? 'block' : 'none';

    tasks.forEach(task => {
        const tr = document.createElement('tr');
        const deadline = task.deadline ? new Date(task.deadline).toLocaleString('pt-BR') : '-';
        tr.innerHTML = `
            <td><input type="checkbox" class="task-checkbox" data-id="${task.id}"></td>
            <td>${task.id}</td>
            <td>${escapeHtml(task.name)}</td>
            <td>${escapeHtml(task.description || '-')}</td>
            <td>${deadline}</td>
            <td>${task.priority}</td>
            <td>${task.status}</td>
            <td>${task.hasAlarm ? 'Sim' : 'Nao'}</td>
            <td>
                <button class="btn-edit" data-id="${task.id}">Editar</button>
                <button class="btn-delete" data-id="${task.id}">Excluir</button>
            </td>`;
        tbody.appendChild(tr);
    });
    document.getElementById('select-all').checked = false;
}