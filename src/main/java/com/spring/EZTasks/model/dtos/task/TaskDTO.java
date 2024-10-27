package com.spring.EZTasks.model.dtos.task;


import com.spring.EZTasks.model.entities.project.Project;
import com.spring.EZTasks.model.entities.task.Comment;
import com.spring.EZTasks.model.entities.task.SubTask;
import com.spring.EZTasks.model.entities.task.Tag;
import com.spring.EZTasks.model.entities.user.User;
import com.spring.EZTasks.utils.enums.project.Status;
import com.spring.EZTasks.utils.enums.task.Priority;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private Project project;
    private List<User> assignees;
    private List<Tag> tags;
    private List<SubTask> subTasks;
    private List<Comment> comments;

    public TaskDTO(String title, List<Tag> tags, Status status, Project project, Priority priority, String description, LocalDate deadline, List<User> assignees) {
        this.title = title;
        this.tags = tags;
        this.status = status;
        this.project = project;
        this.priority = priority;
        this.description = description;
        this.deadline = deadline;
        this.assignees = assignees;
    }
}
