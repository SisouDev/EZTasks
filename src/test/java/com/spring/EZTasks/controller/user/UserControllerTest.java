package com.spring.EZTasks.controller.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.EZTasks.model.dtos.user.UserDTO;
import com.spring.EZTasks.utils.enums.user.Sector;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

   final String baseUrl = "/users";

    @Test
    public void testCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO(null, "John", "john@gmail.com", "password123", "Engenheiro", Sector.ENG);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(post(baseUrl + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@gmail.com"));
    }

    @Test
    public void testGetUser() throws Exception {
        mockMvc.perform(get(baseUrl + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gabriel"))
                .andExpect(jsonPath("$.email").value("gabriel@gmail.com"))
                .andExpect(jsonPath("$.password").value("senha123"))
                .andExpect(jsonPath("$.sector").value(Sector.ENG.toString()))
                .andExpect(jsonPath("$.role").value("Engenheiro"));
    }

    @Test
    public void testFindAllUsers() throws Exception {
        mockMvc.perform(get(baseUrl + "/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserDTO updatedUserDTO = new UserDTO(null, "John", "john_updated@gmail.com", "newpassword123", "Engenheiro", Sector.ENG);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(updatedUserDTO);

        mockMvc.perform(put(baseUrl + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john_updated@gmail.com"))
                .andExpect(jsonPath("$.password").value("newpassword123"))
                .andExpect(jsonPath("$.role").value("Engenheiro"))
                .andExpect(jsonPath("$.sector").value(Sector.ENG.toString()));
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete(baseUrl + "/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get(baseUrl + "/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateUserWithInvalidEmail() throws Exception {
        UserDTO userDTO = new UserDTO(null, "John", "invalid-email", "password123", "Engenheiro", Sector.ENG);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userDTO);
        mockMvc.perform(post(baseUrl + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserWithInvalidData() throws Exception {
        UserDTO updatedUserDTO = new UserDTO(null, "", "", "short", "Engenheiro", Sector.ENG);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(updatedUserDTO);

        mockMvc.perform(put(baseUrl + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        mockMvc.perform(get(baseUrl + "/email")
                        .param("email", "gabriel@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("gabriel@gmail.com"))
                .andExpect(jsonPath("$.name").value("Gabriel"));
    }

    @Test
    public void testGetUserByName() throws Exception {
        mockMvc.perform(get(baseUrl + "/name")
                        .param("name", "Gabriel")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gabriel"))
                .andExpect(jsonPath("$.email").value("gabriel@gmail.com"));
    }

    @Test
    public void testUpdateUserName() throws Exception {
        mockMvc.perform(put(baseUrl + "/{id}/name", 1)
                        .param("name", "John Updated")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"));
    }

    @Test
    public void testUpdateUserEmail() throws Exception {
        mockMvc.perform(put(baseUrl + "/{id}/email", 1)
                        .param("email", "john_updated@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john_updated@gmail.com"));
    }

    @Test
    public void testUpdateUserPassword() throws Exception {
        mockMvc.perform(put(baseUrl + "/{id}/password", 1)
                        .param("password", "newPassword123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").value("newPassword123"));
    }

    @Test
    public void testUpdateUserNameWithInvalidData() throws Exception {
        mockMvc.perform(put(baseUrl + "/{id}/name", 1)
                        .param("name", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserEmailWithInvalidData() throws Exception {
        mockMvc.perform(put(baseUrl + "/{id}/email", 1)
                        .param("email", "invalid-email")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserPasswordWithInvalidData() throws Exception {
        mockMvc.perform(put(baseUrl + "/{id}/password", 1)
                        .param("password", "short")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
