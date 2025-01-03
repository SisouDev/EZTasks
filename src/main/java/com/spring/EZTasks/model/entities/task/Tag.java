package com.spring.EZTasks.model.entities.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.EZTasks.utils.enums.task.Color;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Tag implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Color color;

    @ManyToMany(mappedBy = "tags")
    @ToString.Exclude
    @JsonIgnore
    private List<Task> tasks;

    public Tag(String name, Color color) {
        this.name = name;
        this.color = color;
    }
}
