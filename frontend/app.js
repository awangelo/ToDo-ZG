import {renderTasks} from "./ui.js";
import {deleteTask, saveTask, updateMultipleTasksStatus} from "./taskService.js";
import {clearForm, currentEditingId, fillFormForEdit, getFormData} from "./form.js";
import {validateTask} from "./validation.js";

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
