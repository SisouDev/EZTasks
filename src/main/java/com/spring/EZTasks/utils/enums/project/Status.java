package com.spring.EZTasks.utils.enums.project;

import lombok.Getter;

@Getter
public enum Status {
    PENDING ("Pending"),
    IN_PROGRESS ("In progress"),
    COMPLETED ("Completed"),
    CANCELLED ("Canceled");

    private String status;

    Status(String status) {
        this.status = status;
    }

}
