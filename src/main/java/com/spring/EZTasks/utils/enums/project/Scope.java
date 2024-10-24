package com.spring.EZTasks.utils.enums.project;

import lombok.Getter;

@Getter
public enum Scope {
    ENG("Engineering"),
    IT("Technology"),
    MKT("Marketing"),
    COM("Commercial");

    private String value;
    Scope(String value) {
        this.value = value;
    }
}
