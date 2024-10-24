package com.spring.EZTasks.model.services.user;

import com.spring.EZTasks.model.dtos.user.UserDTO;
import com.spring.EZTasks.model.entities.user.User;
import com.spring.EZTasks.model.repositories.user.UserRepository;
import com.spring.EZTasks.utils.enums.user.Sector;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")

public class UserServiceTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void shouldCreateUserSuccessfully() {
        UserDTO userDTO = new UserDTO(null, "John Doe", "john@example.com", "password123", "USER", Sector.IT);
        User user = new User("john@example.com", "John Doe", "password123", "USER", Sector.IT);
        user.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO createdUser = userService.create(userDTO);

        assertNotNull(createdUser);
        assertEquals(1L, createdUser.getId());
        assertEquals("John Doe", createdUser.getName());
        assertEquals("john@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        User existingUser = new User("john@example.com", "John Doe", "password123", "USER", Sector.IT);
        existingUser.setId(1L);

        UserDTO updatedDTO = new UserDTO(1L, "John Smith", "johnsmith@example.com", "newpassword123", "ADMIN", Sector.IT);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        UserDTO result = userService.updateUser(1L, updatedDTO);

        assertNotNull(result);
        assertEquals("John Smith", result.getName());
        assertEquals("johnsmith@example.com", result.getEmail());
        assertEquals("ADMIN", result.getRole());
        assertEquals(Sector.IT, result.getSector());

        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenCreatingInvalidUser() {
        UserDTO invalidUserDTO = new UserDTO(null, "John Doe", "john@example.com", "123", "USER", Sector.IT);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.create(invalidUserDTO);
        });

        assertEquals("Invalid user", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundForUpdate() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.updateUser(1L, new UserDTO(1L, "John Smith", "johnsmith@example.com", "newpassword123", "ADMIN", Sector.IT));
        });
        assertEquals("User with id 1 not found", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        User existingUser = new User("john@example.com", "John Doe", "password123", "USER", Sector.IT);
        existingUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        doNothing().when(userRepository).deleteById(1L);
        userService.deleteUser(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundForDelete() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });
        assertEquals("User with id 1 not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).deleteById(1L);
    }
    @Test
    void shouldFindAllUsersSuccessfully() {
        List<User> users = List.of(
                new User("john@example.com", "John Doe", "password123", "USER", Sector.IT),
                new User("jane@example.com", "Jane Doe", "password456", "ADMIN", Sector.IT)
        );
        when(userRepository.findAll()).thenReturn(users);
        List<UserDTO> foundUsers = userService.findAll();
        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundForFindById() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.findById(1L);
        });
        assertEquals("User with id 1 not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

}
