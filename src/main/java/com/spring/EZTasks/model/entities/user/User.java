package com.spring.EZTasks.model.entities.user;

import com.spring.EZTasks.utils.enums.Sector;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
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
    @Setter
    @NotEmpty(message = "Name cannot be empty.")
    private String name;

    @Column(nullable = false, unique = true)
    @Setter
    @Email(message = "Email is not valid.")
    @NotEmpty(message = "Email cannot be empty.")
    private String email;

    @Column(nullable = false)
    @Setter
    private String password;

    @Column(nullable = false)
    @Setter
    private String role;

    @Column(nullable = false)
    @Setter
    @Enumerated(EnumType.STRING)
    private Sector sector;

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
