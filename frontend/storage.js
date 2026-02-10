import {STORAGE_KEY} from "./constants.js";

export const loadTasks = () =>
    JSON.parse(localStorage.getItem(STORAGE_KEY)) || [];

export const saveTasks = (tasks) =>
    localStorage.setItem(STORAGE_KEY, JSON.stringify(tasks));
