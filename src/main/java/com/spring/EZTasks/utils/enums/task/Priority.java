package com.spring.EZTasks.utils.enums.task;

import lombok.Getter;

@Getter
public enum Priority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    URGENT("Urgent");
    private String value;

    Priority(String value) {
        this.value = value;
    }
}
