package com.spring.EZTasks.utils.enums.user;

import lombok.Getter;

@Getter
public enum Sector {
    ENG("Engineering"),
    IT("Technology"),
    MKT("Marketing"),
    COM("Commercial");

    private final String sector;

    Sector(String sector) {
        this.sector = sector;
    }
}
