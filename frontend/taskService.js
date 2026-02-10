import {PRIORITY_VALUES} from "./constants.js";
import {loadTasks, saveTasks} from "./storage.js";
import {currentEditingId} from "./form.js";

export function saveTask(taskData) {
    const tasks = loadTasks();

    if (currentEditingId === null) {
        const newId = Math.max(0, ...tasks.map(t => t.id)) + 1;
        tasks.push({id: newId, ...taskData});
        saveTasks(tasks);
        return true;
    }

    const index = tasks.findIndex(t => t.id === currentEditingId);
    if (index === -1) return false;

    // {old, new} propiedades nao substituidas permancem (shallow copy)
    tasks[index] = {...tasks[index], ...taskData};
    saveTasks(tasks);
    return true;
}

export function deleteTask(id) {
    const tasks = loadTasks();
    const index = tasks.findIndex(t => t.id === id);
    if (index === -1) return false;

    tasks.splice(index, 1);
    saveTasks(tasks);
    return true;
}

export function updateMultipleTasksStatus(ids, newStatus) {
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
export function sortTasks(tasks) {
    return [...tasks].sort((a, b) => {
        const pDiff = PRIORITY_VALUES[b.priority] - PRIORITY_VALUES[a.priority];
        if (pDiff !== 0) return pDiff;
        if (!a.deadline && !b.deadline) return 0;
        if (!a.deadline) return 1;
        if (!b.deadline) return -1;
        return new Date(a.deadline) - new Date(b.deadline);
    });
}

export function getFilteredTasks() {
    const filter = document.querySelector('input[name="filter"]:checked').value;
    const tasks = loadTasks();
    return filter ? tasks.filter(t => t.status === filter) : tasks;
}
