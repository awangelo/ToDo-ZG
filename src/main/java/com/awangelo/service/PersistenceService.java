package com.awangelo.service;

import com.awangelo.model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class PersistenceService {
    private static final String DEFAULT_FILENAME = "tasks.bin";

    public void save(List<Task> tasks, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(new ArrayList<>(tasks));
        }
    }

    public void save(List<Task> tasks) throws IOException {
        save(tasks, DEFAULT_FILENAME);
    }

    @SuppressWarnings("unchecked")
    public List<Task> load(String filename) throws IOException, ClassNotFoundException {
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Task>) ois.readObject();
        }
    }

    public List<Task> load() throws IOException, ClassNotFoundException {
        return load(DEFAULT_FILENAME);
    }
}