package com.spring.EZTasks.model.entities.user;

import com.spring.EZTasks.utils.enums.Sector;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {
    @Test
    public void testUserCreation() {
        User user = new User(
                "fulano@gmail.com", "Fulano",
                "senha123", "Engenheiro",
                Sector.ENG
        );
        assertNotNull(user);
        assertEquals("fulano@gmail.com", user.getEmail());
        assertEquals("senha123", user.getPassword());
        assertEquals(Sector.ENG, user.getSector());
    }
}
