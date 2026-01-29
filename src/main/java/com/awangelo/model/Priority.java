package com.awangelo.model;

public enum Priority {
    VERY_LOW(1),
    LOW(2),
    MEDIUM(3),
    HIGH(4),
    VERY_HIGH(5);

    private final int value;

    Priority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Priority fromValue(int value) {
        for (Priority p : Priority.values()) {
            if (p.value == value) {
                return p;
            }
        }
        throw new IllegalArgumentException(
                "Invalid priority value: " + value + ". Must be between 1 and 5.");
    }
}