package com.spring.EZTasks.model.services;

import com.spring.EZTasks.model.dtos.user.UserDTO;
import com.spring.EZTasks.model.entities.user.User;
import com.spring.EZTasks.model.repositories.user.UserRepository;
import com.spring.EZTasks.model.services.user.UserService;
import com.spring.EZTasks.utils.enums.Sector;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUser_validUser() {
        UserDTO userDTO = new UserDTO(1L,"John", "john@gmail.com", "password123", "Engenheiro", Sector.ENG);
        User user = new User("john@gmail.com", "John", "password123", "Engenheiro", Sector.ENG);

        Mockito.when(userRepository
                .save(Mockito.any(User.class)))
                .thenReturn(new User("john@gmail.com", "John", "password123", "Engenheiro", Sector.ENG));


        UserDTO createdUser = userService.create(userDTO);
        assertNotNull(createdUser);
        assertEquals("John", createdUser.getName());
        assertEquals("john@gmail.com", createdUser.getEmail());
    }

    @Test
    public void testCreateUser_invalidUser() {
        UserDTO userDTO = new UserDTO(null, "Jo", "jo", "123", "Eng", null);
        assertThrows(EntityNotFoundException.class, () -> {
            userService.create(userDTO);
        });
    }

}
