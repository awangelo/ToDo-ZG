export function validateTask(data) {
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