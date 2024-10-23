package com.spring.EZTasks.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.EZTasks.model.dtos.user.UserDTO;
import com.spring.EZTasks.model.entities.user.User;
import com.spring.EZTasks.utils.enums.Sector;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

   final String baseUrl = "/users;";

    @Test
    public void testCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO(null, "John", "john@gmail.com", "password123", "Engenheiro", Sector.ENG);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@gmail.com"));
    }

    @Test
    public void testGetUser() throws Exception {
        mockMvc.perform(get(baseUrl + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@gmail.com"))
                .andExpect(jsonPath("$.password").value("123456"))
                .andExpect(jsonPath("$.sector").value(Sector.ENG))
                .andExpect(jsonPath("$.role").value("Engenheiro"));
    }
}
