package com.spring.EZTasks.model.entities.user;

import com.spring.EZTasks.model.entities.project.Project;
import com.spring.EZTasks.utils.enums.user.Sector;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
@Table(name = "`user`")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "Name cannot be empty.")
    private String name;

    @Column(nullable = false, unique = true)
    @Email(message = "Email is not valid.")
    @NotEmpty(message = "Email cannot be empty.")
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sector sector;

    @ManyToMany(mappedBy = "members")
    @ToString.Exclude
    private List<Project> projectsAsMember = new ArrayList<>();

    @OneToMany(mappedBy = "leader")
    @ToString.Exclude
    private List<Project> projectsAsLeader = new ArrayList<>();

    public User(String email, String name, String password, String role, Sector sector) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.sector = sector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
