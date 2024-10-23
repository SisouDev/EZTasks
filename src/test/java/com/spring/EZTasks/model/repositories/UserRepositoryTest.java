package com.spring.EZTasks.model.repositories;

import com.spring.EZTasks.model.entities.user.User;
import com.spring.EZTasks.model.repositories.user.UserRepository;
import com.spring.EZTasks.utils.enums.Sector;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmail() {
        User user = new User("john@gmail.com", "John", "password123", "Engenheiro", Sector.ENG);
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("john@gmail.com");
        assertTrue(foundUser.isPresent());
        assertEquals("john@gmail.com", foundUser.get().getEmail());
    }
}
