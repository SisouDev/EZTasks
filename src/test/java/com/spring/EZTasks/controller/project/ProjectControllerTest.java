package com.spring.EZTasks.controller.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.EZTasks.model.dtos.project.ProjectDTO;
import com.spring.EZTasks.model.entities.user.User;
import com.spring.EZTasks.utils.enums.project.Scope;
import com.spring.EZTasks.utils.enums.project.Status;
import com.spring.EZTasks.utils.enums.user.Sector;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProjectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    final String baseUrl = "/projects";

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCreateProject() throws Exception {
        User user1 = new User("oliver@gmail.com", "Oliver", "senha123", "Engenheiro", Sector.ENG);
        var projectDTO =  new ProjectDTO(null, LocalDate.of(2025, 1, 15), "Projeto de automacao2", user1, "Automacao2", Scope.ENG, Status.IN_PROGRESS);
        user1.setId(1L);

        mockMvc.perform(post(baseUrl + "/create/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDTO)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void shouldUpdateProject() throws Exception {
        User user1 = new User("kaio@gmail.com", "Kaio", "senha123", "Engenheiro", Sector.ENG);
        user1.setId(1L);
        var updatedProjectDTO = new ProjectDTO(null, LocalDate.of(2025, 1, 15), "Projeto legal", user1, "Projeto Legal", Scope.ENG, Status.IN_PROGRESS);

        mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProjectDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Projeto Legal"))
                .andDo(print());
    }

    @Test
    public void shouldGetProjectById() throws Exception {
        mockMvc.perform(get(baseUrl + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andDo(print());
    }

    @Test
    public void shouldGetAllProjects() throws Exception {
        mockMvc.perform(get(baseUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNotEmpty())
                .andDo(print());
    }

    @Test
    public void shouldDeleteProject() throws Exception {
        mockMvc.perform(delete(baseUrl + "/1"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void shouldUpdateProjectName() throws Exception {
        String newName = "Updated Name";

        mockMvc.perform(patch(baseUrl + "/1/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((newName)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(newName))
                .andDo(print());
    }

    @Test
    public void shouldGetProjectsByStatus() throws Exception {
        mockMvc.perform(get(baseUrl + "/byStatus")
                        .param("status", "IN_PROGRESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNotEmpty())
                .andDo(print());
    }

    @Test
    public void shouldAddMemberToProject() throws Exception {
        mockMvc.perform(patch(baseUrl + "/1/addMember")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(2L)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void shouldRemoveMemberFromProject() throws Exception {
        mockMvc.perform(patch(baseUrl + "/1/removeMember")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(2L)))
                .andExpect(status().isOk())
                .andDo(print());
    }

}
