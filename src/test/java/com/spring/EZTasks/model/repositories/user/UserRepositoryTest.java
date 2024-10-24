package com.spring.EZTasks.model.repositories.user;

import com.spring.EZTasks.model.entities.user.User;
import com.spring.EZTasks.utils.enums.user.Sector;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindUserByEmail() {
        Optional<User> foundUser = userRepository.findByEmail("tiago@gmail.com");
        assertTrue(foundUser.isPresent());
        assertEquals("tiago@gmail.com", foundUser.get().getEmail());
        assertEquals("Tiago Paulo", foundUser.get().getName());
    }

    @Test
    void shouldFindUserById() {
        User user = userRepository.findByEmail("afonso@ig.com").orElseThrow();
        Optional<User> foundUser = userRepository.findById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("afonso@ig.com", foundUser.get().getEmail());
        assertEquals("Afonso Luis", foundUser.get().getName());
    }

    @Test
    void shouldFindUsersBySector() {
        List<User> users = userRepository.findAllBySector(Sector.MKT);
        assertFalse(users.isEmpty());
        assertEquals(2, users.size());
    }

    @Test
    void shouldFindBySectorAndName(){
        Optional<User> user = userRepository.findBySectorAndName(Sector.IT, "Joao Pedro");
        assertTrue(user.isPresent());
        assertEquals("Joao Pedro", user.get().getName());
    }

    @Test
    void findAllByRole(){
        List<User> users = userRepository.findAllByRole("Engenheiro");
        assertFalse(users.isEmpty());
        assertEquals(3, users.size());
    }

    @Test
    void shouldUpdateNameById(){
        User user = userRepository.findById(1L).orElseThrow();
        user.setName("Gabrielle");
        userRepository.save(user);
        assertEquals("Gabrielle", user.getName());
    }

    @Test
    void shouldUpdateSectorById(){
        User user = userRepository.findById(1L).orElseThrow();
        user.setSector(Sector.MKT);
        userRepository.save(user);
        assertEquals(Sector.MKT, user.getSector());
    }

    @Test
    void shouldUpdateRoleById(){
        User user = userRepository.findById(1L).orElseThrow();
        user.setRole("Analista");
        userRepository.save(user);
        assertEquals("Analista", user.getRole());
    }

    @Test
    void shouldUpdateEmailById(){
        User user = userRepository.findById(1L).orElseThrow();
        user.setEmail("gabrielle@gmail.com");
        userRepository.save(user);
        assertEquals("gabrielle@gmail.com", user.getEmail());
    }

    @Test
    void shouldUpdatePasswordById(){
        User user = userRepository.findById(1L).orElseThrow();
        user.setPassword("123456");
        userRepository.save(user);
        assertEquals("123456", user.getPassword());
    }

    @Test
    void shouldDeleteUserById(){
        User user = userRepository.findById(1L).orElseThrow();
        userRepository.delete(user);
        Optional<User> foundUser = userRepository.findById(user.getId());
        assertFalse(foundUser.isPresent());
    }

    @Test
    void shouldFindUsersByNameContainingIgnoreCase(){
        List<User> users = userRepository.findUsersByNameContainingIgnoreCase("Joao");
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
    }

    @Test
    void shouldFindUsersByNameIsLikeIgnoreCase(){
        List<User> users = userRepository.findUsersByNameIsLikeIgnoreCase("ti%");
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
    }

    @Test
    void shouldFindUsersByNameStartingWith(){
        List<User> users = userRepository.findUsersByNameStartingWith("t");
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
    }

    @Test
    void shouldFindUsersByNameEndingWith(){
        List<User> users = userRepository.findUsersByNameEndingWith("o");
        assertFalse(users.isEmpty());
        assertEquals(2, users.size());
    }


}
