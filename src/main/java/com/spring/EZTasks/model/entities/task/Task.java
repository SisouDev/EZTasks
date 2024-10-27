package com.spring.EZTasks.model.entities.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.EZTasks.model.entities.project.Project;
import com.spring.EZTasks.model.entities.user.User;
import com.spring.EZTasks.utils.enums.project.Status;
import com.spring.EZTasks.utils.enums.task.Priority;
import com.spring.EZTasks.utils.enums.task.Color;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Task implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotEmpty
    @NotNull
    private String title;

    @Column(nullable = false)
    @NotEmpty
    @NotNull
    private String description;

    @Column(nullable = false)
    private LocalDate deadline;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore
    private Project project;

    @ManyToMany
    @JoinTable(
            name = "task_assignees",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @ToString.Exclude
    @JsonIgnore
    private List<User> assignees;

    @ManyToMany
    @JoinTable(
            name = "task_tags",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @ToString.Exclude
    @JsonIgnore
    private List<Tag> tags;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<SubTask> subTasks = new ArrayList<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    public Task(Long id, String title, String description, Status status, Project project, Priority priority, LocalDate deadline, List<User> assignees, List<Tag> tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.project = project;
        this.priority = priority;
        this.deadline = deadline;
        this.assignees = assignees;
        this.tags = tags;
    }
    public Task(String title, String description, Status status, Project project, Priority priority, LocalDate deadline, List<User> assignees, List<Tag> tags) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.project = project;
        this.priority = priority;
        this.deadline = deadline;
        this.assignees = assignees;
        this.tags = tags;
    }

    public Task(String title, String description, Status status, Project project, Priority priority, LocalDate deadline, List<Tag> tags) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.project = project;
        this.priority = priority;
        this.deadline = deadline;
        this.tags = tags;
    }

}
