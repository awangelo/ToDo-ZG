const PRIORITY_VALUES = { 1: 1, 2: 2, 3: 3, 4: 4, 5: 5 };
const STORAGE_KEY = 'todoTasks';
let currentEditingId = null; // Tarefa sendo criada/editada

// LocalStorage
const loadTasks = () => JSON.parse(localStorage.getItem(STORAGE_KEY)) || [];
const saveTasks = (tasks) => localStorage.setItem(STORAGE_KEY, JSON.stringify(tasks));

function saveTask(taskData) {
    const tasks = loadTasks();

    if (currentEditingId === null) {
        const newId = Math.max(0, ...tasks.map(t => t.id)) + 1;
        tasks.push({ id: newId, ...taskData });
        saveTasks(tasks);
        return true;
    }

    const index = tasks.findIndex(t => t.id === currentEditingId);
    if (index === -1) return false;
    
    // {old, new} propiedades nao substituidas permancem (shallow copy) 
    tasks[index] = { ...tasks[index], ...taskData };
    saveTasks(tasks);
    return true;
}

function deleteTask(id) {
    const tasks = loadTasks();
    const index = tasks.findIndex(t => t.id === id);
    if (index === -1) return false;

    tasks.splice(index, 1);
    saveTasks(tasks);
    return true;
}

function updateMultipleTasksStatus(ids, newStatus) {
    const tasks = loadTasks();
    let count = 0;
    ids.forEach(id => {
        const task = tasks.find(t => t.id === id);
        if (task) {
            task.status = newStatus;
            count++;
        }
    });
    saveTasks(tasks);
    return count;
}

// Ordenacao e Filtro
function sortTasks(tasks) {
    return [...tasks].sort((a, b) => {
        const pDiff = PRIORITY_VALUES[b.priority] - PRIORITY_VALUES[a.priority];
        if (pDiff !== 0) return pDiff;
        if (!a.deadline && !b.deadline) return 0;
        if (!a.deadline) return 1;
        if (!b.deadline) return -1;
        return new Date(a.deadline) - new Date(b.deadline);
    });
}

function getFilteredTasks() {
    const filter = document.querySelector('input[name="filter"]:checked').value;
    const tasks = loadTasks();
    return filter ? tasks.filter(t => t.status === filter) : tasks;
}

function validateTask(data) {
    if (!data.name || !data.name.trim()) {
        alert('O nome da tarefa e obrigatorio!');
        return false;
    }

    // Prioridade: deve ser 1-5
    const priorityRegex = /^[1-5]$/;
    if (!priorityRegex.test(data.priority.toString())) {
        alert('Prioridade deve ser um numero de 1 a 5!');
        return false;
    }

    // Deadline: formato YYYY-MM-DDTHH:mm e futura
    if (data.deadline) {
        const deadlineRegex = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}$/;
        if (!deadlineRegex.test(data.deadline)) {
            alert('Deadline deve estar no formato YYYY-MM-DDTHH:mm!');
            return false;
        }
        if (new Date(data.deadline) <= new Date()) {
            alert('Deadline deve ser no futuro!');
            return false;
        }
    }

    // Alarme requer deadline no futuro
    if (data.hasAlarm) {
        if (!data.deadline) {
            alert('Para ter alarme, defina um deadline!');
            return false;
        }
        if (new Date(data.deadline) <= new Date()) {
            alert('Para ter alarme, o deadline deve ser no futuro!');
            return false;
        }
    }

    return true;
}

function getFormData() {
    return {
        name: document.getElementById('task-name').value,
        description: document.getElementById('task-description').value || '', // opcional
        deadline: document.getElementById('task-deadline').value || null, // opcional
        priority: parseInt(document.getElementById('task-priority').value) || 3,
        status: document.getElementById('task-status').value,
        hasAlarm: document.getElementById('task-alarm').checked
    };
}

function clearForm() {
    document.getElementById('task-form').reset();
    document.getElementById('task-priority').value = 3;
    document.getElementById('form-title').textContent = 'Nova Tarefa';
    currentEditingId = null;
}

function fillFormForEdit(id) {
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

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function renderTasks() {
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

document.addEventListener('DOMContentLoaded', () => {
    renderTasks();

    document.getElementById('task-form').addEventListener('submit', (e) => {
        e.preventDefault();
        const data = getFormData();
        if (!validateTask(data)) return;

        saveTask(data);
        alert(currentEditingId !== null ? 'Tarefa atualizada!' : 'Tarefa criada!');
        clearForm();
        renderTasks();
    });

    document.getElementById('btn-cancel').addEventListener('click', clearForm);
    document.getElementById('filter-status').addEventListener('change', renderTasks);

    document.getElementById('select-all').addEventListener('change', function() {
        document.querySelectorAll('.task-checkbox').forEach(cb => cb.checked = this.checked);
    });

    document.getElementById('btn-bulk-update').addEventListener('click', () => {
        const ids = Array.from(document.querySelectorAll('.task-checkbox:checked')).map(cb => parseInt(cb.dataset.id));
        if (ids.length === 0) {
            alert('Selecione pelo menos uma tarefa!');
            return;
        }

        const status = document.getElementById('bulk-status').value;
        const count = updateMultipleTasksStatus(ids, status);
        alert(count + ' tarefa(s) atualizada(s) para ' + status + '!');
        renderTasks();
    });

    document.getElementById('tasks-body').addEventListener('click', (e) => {
        const id = parseInt(e.target.dataset.id);
        if (e.target.classList.contains('btn-edit')) fillFormForEdit(id);
        if (e.target.classList.contains('btn-delete') && confirm('Deseja excluir esta tarefa?')) {
            deleteTask(id);
            alert('Tarefa excluida!');
            renderTasks();
        }
    });
});
