package com.spring.EZTasks.utils.enums.task;

import lombok.Getter;

@Getter
public enum Color {
    WHITE("White"),
    GREEN("Green"),
    YELLOW("Yellow"),
    ORANGE("Orange"),
    BLUE("Blue"),
    RED("Red");

    private String value;

    Color(String value) {
        this.value = value;
    }
}
