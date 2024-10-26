package com.spring.EZTasks.model.repositories.project;

import com.spring.EZTasks.model.entities.project.Project;
import com.spring.EZTasks.utils.enums.project.Scope;
import com.spring.EZTasks.utils.enums.project.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class ProjectRepositoryTest {
    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void shouldFindByName(){
        Optional<Project> foundedProject = projectRepository.findByName("Automacao");
        assertTrue(foundedProject.isPresent());
        assertEquals("Automacao", foundedProject.get().getName());
        assertEquals("Projeto de automacao", foundedProject.get().getDescription());
    }

    @Test
    void shouldFindByLeaderId() {
        Optional<Project> project = projectRepository.findByLeaderId(1L);
        assertTrue(project.isPresent());
        assertEquals(1L, project.get().getLeader().getId());
    }

    @Test
    void shouldFindAllByStatus() {
        List<Project> projects = projectRepository.findAllByStatus(Status.COMPLETED);
        assertFalse(projects.isEmpty());
        assertTrue(projects.stream().allMatch(p -> p.getStatus() == Status.COMPLETED));
    }

    @Test
    void shouldFindAllByMembersId() {
        List<Project> projects = projectRepository.findAllByMembersId(4L);
        assertFalse(projects.isEmpty());
    }

    @Test
    void shouldFindAllByDeadlineBetween() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 12, 31);
        List<Project> projects = projectRepository.findAllByDeadlineBetween(start, end);
        assertFalse(projects.isEmpty());
    }

    @Test
    void shouldCountAllByStatus() {
        int count = projectRepository.countAllByStatus(Status.COMPLETED);
        assertTrue(count > 0);
    }

    @Test
    void shouldUpdateProjectNameById() {
        int updated = projectRepository.updateProjectNameById(1L, "New Project Name");
        assertEquals(1, updated);
        Optional<Project> project = projectRepository.findById(1L);
        assertTrue(project.isPresent());
        assertEquals("New Project Name", project.get().getName());
    }

    @Test
    void shouldAddAndRemoveMemberFromProject() {
        int added = projectRepository.addMemberToProject(1L, 3L);
        assertEquals(1, added);

        int removed = projectRepository.removeMemberFromProject(1L, 3L);
        assertEquals(1, removed);
    }

    @Test
    void shouldUpdateStatusById() {
        int updated = projectRepository.updateStatusById(1L, Status.COMPLETED);
        assertEquals(1, updated);
        Optional<Project> project = projectRepository.findById(1L);
        assertTrue(project.isPresent());
        assertEquals(Status.COMPLETED, project.get().getStatus());
    }

    @Test
    void shouldFindByLeaderIdAndDescription() {
        Optional<Project> project = projectRepository.findByLeaderIdAndDescription(1L, "Projeto de automacao");
        assertTrue(project.isPresent());
    }

    @Test
    void shouldFindAllByNameNotContainsIgnoreCase() {
        List<Project> projects = projectRepository.findAllByNameNotContainsIgnoreCase("test");
        assertFalse(projects.isEmpty());
    }

    @Test
    void shouldFindAllByScope() {
        List<Project> projects = projectRepository.findAllByScope(Scope.ENG);
        assertFalse(projects.isEmpty());
        assertTrue(projects.stream().allMatch(p -> p.getScope() == Scope.ENG));
    }

    @Test
    void shouldUpdateDeadlineById() {
        LocalDate newDeadline = LocalDate.of(2025, 6, 30);
        int updated = projectRepository.updateDeadlineById(1L, newDeadline);
        assertEquals(1, updated);
        Optional<Project> project = projectRepository.findById(1L);
        assertTrue(project.isPresent());
        assertEquals(newDeadline, project.get().getDeadline());
    }

}
