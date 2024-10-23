package com.spring.EZTasks.model.dtos.user;

import com.spring.EZTasks.utils.enums.Sector;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class UserDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;
    private Sector sector;

    // TODO config userdto id problem with user id was not generated after save in userService
    public UserDTO(String email, String name, String password, String role, Sector sector) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.sector = sector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO userDTO)) return false;
        return Objects.equals(getId(), userDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
