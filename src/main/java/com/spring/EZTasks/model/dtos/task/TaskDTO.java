package com.spring.EZTasks.model.dtos.task;

import com.spring.EZTasks.model.dtos.project.ProjectDTO;
import com.spring.EZTasks.model.dtos.user.UserDTO;
import com.spring.EZTasks.model.entities.task.Tag;
import com.spring.EZTasks.utils.enums.project.Status;
import com.spring.EZTasks.utils.enums.task.Priority;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate deadline;
    private Status status;
    private Priority priority;
    private ProjectDTO project;
    private List<UserDTO> assignees;
    private List<Tag> tags;
}
