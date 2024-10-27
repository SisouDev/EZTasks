package com.spring.EZTasks.controller.project;

import com.spring.EZTasks.model.dtos.project.ProjectDTO;
import com.spring.EZTasks.model.services.project.ProjectService;
import com.spring.EZTasks.utils.enums.project.Scope;
import com.spring.EZTasks.utils.enums.project.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/projects")
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping(value = "/create/{leaderId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProjectDTO> createProject(@PathVariable Long leaderId, @RequestBody ProjectDTO projectDTO) {
        ProjectDTO createdProject = projectService.createProject(projectDTO, leaderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        ProjectDTO updatedProject = projectService.updateProjectById(id, projectDTO);
        return ResponseEntity.ok(updatedProject);
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<ProjectDTO> updateProjectName(@PathVariable Long id, @RequestBody String name) {
        ProjectDTO updatedProject = projectService.updateNameById(id, name);
        return ResponseEntity.ok(updatedProject);
    }

    @PatchMapping("/{id}/description")
    public ResponseEntity<ProjectDTO> updateProjectDescription(@PathVariable Long id, @RequestBody String description) {
        ProjectDTO updatedProject = projectService.updateDescriptionById(id, description);
        return ResponseEntity.ok(updatedProject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        ProjectDTO project = projectService.findById(id);
        return ResponseEntity.ok(project);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = projectService.findAll();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/byStatus")
    public ResponseEntity<List<ProjectDTO>> getProjectsByStatus(@RequestParam Status status) {
        List<ProjectDTO> projects = projectService.findAllByStatus(status);
        return ResponseEntity.ok(projects);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProjectById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProjectDTO> updateProjectStatus(@PathVariable Long id, @RequestBody Status status) {
        ProjectDTO updatedProject = projectService.updateStatusById(id, status);
        return ResponseEntity.ok(updatedProject);
    }

    @PatchMapping("/{id}/scope")
    public ResponseEntity<ProjectDTO> updateProjectScope(@PathVariable Long id, @RequestBody Scope scope) {
        ProjectDTO updatedProject = projectService.updateScopeById(id, scope);
        return ResponseEntity.ok(updatedProject);
    }

    @PatchMapping("/{id}/deadline")
    public ResponseEntity<ProjectDTO> updateProjectDeadline(@PathVariable Long id, @RequestBody LocalDate deadline) {
        ProjectDTO updatedProject = projectService.updateDeadLineById(id, deadline);
        return ResponseEntity.ok(updatedProject);
    }

    @PatchMapping("/{id}/leader")
    public ResponseEntity<ProjectDTO> updateProjectLeader(@PathVariable Long id, @RequestBody Long leaderId) {
        ProjectDTO updatedProject = projectService.updateLeaderById(id, leaderId);
        return ResponseEntity.ok(updatedProject);
    }

    @PatchMapping("/{projectId}/addMember")
    public ResponseEntity<ProjectDTO> addMemberToProject(@PathVariable Long projectId, @RequestBody Long userId) {
        ProjectDTO updatedProject = projectService.addMemberToProject(projectId, userId);
        return ResponseEntity.ok(updatedProject);
    }

    @PatchMapping("/{projectId}/removeMember")
    public ResponseEntity<ProjectDTO> removeMemberFromProject(@PathVariable Long projectId, @RequestBody Long userId) {
        ProjectDTO updatedProject = projectService.removeMemberFromProject(projectId, userId);
        return ResponseEntity.ok(updatedProject);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ProjectDTO> getProjectByName(@PathVariable String name) {
        ProjectDTO project = projectService.findByName(name);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/leader/{leaderId}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByLeader(@PathVariable Long leaderId) {
        List<ProjectDTO> projects = projectService.findAllByLeaderId(leaderId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/scope/{scope}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByScope(@PathVariable Scope scope) {
        List<ProjectDTO> projects = projectService.findAllByScope(scope);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/deadline/after/{date}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByDeadlineAfter(@PathVariable LocalDate date) {
        List<ProjectDTO> projects = projectService.findAllByDeadlineAfter(date);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/deadline/before/{date}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByDeadlineBefore(@PathVariable LocalDate date) {
        List<ProjectDTO> projects = projectService.findAllByDeadlineBefore(date);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/deadline/between")
    public ResponseEntity<List<ProjectDTO>> getProjectsByDeadlineBetween(
            @RequestParam LocalDate start, @RequestParam LocalDate end) {
        List<ProjectDTO> projects = projectService.findAllByDeadlineBetween(start, end);
        return ResponseEntity.ok(projects);
    }

}
