package com.spring.EZTasks.model.dtos.project;

import com.spring.EZTasks.model.entities.user.User;
import com.spring.EZTasks.utils.enums.project.Scope;
import com.spring.EZTasks.utils.enums.project.Status;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate deadline;
    private Status status;
    private Scope scope;
    private User leader;
    private List<User> members = new ArrayList<>();

}
