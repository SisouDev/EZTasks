package com.spring.EZTasks.model.services.project;

import com.spring.EZTasks.config.DataSeed;
import com.spring.EZTasks.model.dtos.project.ProjectDTO;
import com.spring.EZTasks.model.entities.project.Project;
import com.spring.EZTasks.model.entities.user.User;
import com.spring.EZTasks.model.repositories.project.ProjectRepository;
import com.spring.EZTasks.model.repositories.user.UserRepository;
import com.spring.EZTasks.utils.enums.project.Scope;
import com.spring.EZTasks.utils.enums.project.Status;
import com.spring.EZTasks.utils.enums.user.Sector;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(DataSeed.class)
public class ProjectServiceTest {
    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ProjectService projectService;

    @Test
    void shouldCreateProjectSuccessfully() {
        User user1 = new User("oliver@gmail.com", "Oliver", "senha123", "Engenheiro", Sector.ENG);
        user1.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        ProjectDTO projectDTO = new ProjectDTO(null, LocalDate.of(2025, 1, 15), "Projeto de automacao2", user1, "Automacao2", Scope.ENG, Status.IN_PROGRESS);
        Project project = new Project(LocalDate.of(2025, 1, 15), "Projeto de automacao2", user1, "Automacao2", Scope.ENG, Status.IN_PROGRESS);
        project.setId(1L);
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectDTO createdProject = projectService.createProject(projectDTO);

        assertNotNull(createdProject);
        assertEquals(1L, createdProject.getId());
        assertEquals("Automacao2", createdProject.getName());
        assertEquals(projectDTO.getDescription(), createdProject.getDescription());
        assertEquals(projectDTO.getDeadline(), createdProject.getDeadline());

        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void shouldUpdateProjectSuccessfully() {
        User user1 = new User("oliver@gmail.com", "Oliver", "senha123", "Engenheiro", Sector.ENG);
        user1.setId(1L);
        Project existingProject = new Project(LocalDate.of(2025, 1, 15), "Projeto de automacao2", user1, "Automacao2", Scope.ENG, Status.IN_PROGRESS);
        existingProject.setId(1L);

        ProjectDTO updatedProjectDTO = new ProjectDTO(1L, LocalDate.of(2025, 2, 15), "Projeto atualizado", user1, "Automacao3", Scope.ENG, Status.COMPLETED);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenReturn(existingProject);

        ProjectDTO result = projectService.updateProjectById(1L, updatedProjectDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Projeto atualizado", result.getDescription());
        assertEquals(Status.COMPLETED, result.getStatus());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    public void shouldReturnAllProjectsForMember() {
        Long memberId = 1L;
        User member = new User("oliver@gmail.com", "Oliver", "senha123", "Engenheiro", Sector.ENG);
        member.setId(memberId);

        Project project1 = new Project(LocalDate.of(2025, 3, 10), "Projeto de Teste 1", member, "Teste1", Scope.ENG, Status.IN_PROGRESS);
        project1.setId(1L);
        project1.getMembers().add(member);

        Project project2 = new Project(LocalDate.of(2025, 4, 10), "Projeto de Teste 2", member, "Teste2", Scope.ENG, Status.COMPLETED);
        project2.setId(2L);
        project2.getMembers().add(member);
        List<Project> mockProjects = Arrays.asList(project1, project2);
        when(projectRepository.findAllByMembersId(memberId)).thenReturn(mockProjects);
        List<Project> projects = projectService.findAllProjectsByMemberId(memberId);
        assertNotNull(projects);
        assertFalse(projects.isEmpty());
        projects.forEach(project ->
                assertTrue(
                        project.getMembers().stream().anyMatch(m -> m.getId().equals(memberId))
                )
        );
        verify(projectRepository, times(1)).findAllByMembersId(memberId);
    }

    @Test
    public void shouldReturnProjectsForExistingMember() {
        User member = new User("joao@ig.com", "Joao Pedro", "password123", "Engenheiro", Sector.IT);
        member.setId(1L);

        when(userRepository.findByEmail("joao@ig.com")).thenReturn(Optional.of(member));

        Project project1 = new Project(LocalDate.of(2025, 3, 10), "Projeto 1", member, "Teste", Scope.IT, Status.IN_PROGRESS);
        project1.setId(1L);
        project1.getMembers().add(member);

        List<Project> mockProjects = List.of(project1);

        when(projectRepository.findAllByMembersId(1L)).thenReturn(mockProjects);

        List<Project> projects = projectService.findAllProjectsByMemberId(1L);
        assertNotNull(projects);
        assertFalse(projects.isEmpty());
        assertEquals(1, projects.size());

        projects.forEach(project ->
                assertTrue(project.getMembers().stream().anyMatch(m -> m.getId().equals(1L)))
        );
        verify(projectRepository, times(1)).findAllByMembersId(1L);
    }
}

