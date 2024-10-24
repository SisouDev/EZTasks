package com.spring.EZTasks.model.services.project;

import com.spring.EZTasks.model.dtos.project.ProjectDTO;
import com.spring.EZTasks.model.entities.project.Project;
import com.spring.EZTasks.model.entities.user.User;
import com.spring.EZTasks.model.repositories.project.ProjectRepository;
import com.spring.EZTasks.model.repositories.user.UserRepository;
import com.spring.EZTasks.utils.enums.project.Scope;
import com.spring.EZTasks.utils.enums.project.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    private ProjectDTO convertToDTO(Project project) {
        if (project == null) {
            log.info("Project is null");
            throw new IllegalArgumentException("Project cannot be null");
        }
        log.info("Project {}", project);
        log.info(project.getName());
        log.info(project.getDescription());
        log.info(project.getStatus().toString());
        log.info(project.getScope().toString());
        log.info(project.getMembers().toString());

        return new ProjectDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getDeadline(),
                project.getStatus(),
                project.getScope(),
                project.getLeader(),
                project.getMembers()
        );
    }

    private Project convertToEntity(ProjectDTO projectDTO) {
        if (projectDTO == null) {
            log.info("Project DTO is null");
        }
        log.info("Returning Project DTO {}", projectDTO);
        assert projectDTO != null;
        log.info(projectDTO.getName());
        log.info(projectDTO.getDescription());
        log.info(projectDTO.getStatus().toString());
        log.info(projectDTO.getScope().toString());
        log.info(projectDTO.getMembers().toString());
        return new Project(
                projectDTO.getId(),
                projectDTO.getName(),
                projectDTO.getDescription(),
                projectDTO.getDeadline(),
                projectDTO.getStatus(),
                projectDTO.getScope(),
                projectDTO.getLeader(),
                projectDTO.getMembers()
        );
    }

    public boolean isProjectValid(ProjectDTO projectDTO) {
        Project project = convertToEntity(projectDTO);
        if (project.getName() == null || project.getName().length() < 3 || project.getName().length() > 20){
            log.info("Project name is empty or not valid");
            return false;
        } else if (project.getDescription() == null || project.getDescription().length() < 5 || project.getDescription().length() > 200){
            log.info("Project description is empty or not valid");
            return false;
        } else if (project.getLeader().getId() == null) {
            log.info("Project leader Id is null");
            return false;
        } else if (project.getMembers().isEmpty()) {
            log.info("Project members is empty or not valid");
        } else if (project.getDeadline() == null) {
            log.info("Project deadline is empty or not valid");
            return false;
        }
        log.info("Is valid: {}", project.getName());
        return true;
    }

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        if (isProjectValid(projectDTO)) {
            Project project = convertToEntity(projectDTO);
            project = projectRepository.save(project);
            log.info("Successfully created project {}", project);
            return convertToDTO(project);
        } else {
            log.info("Project is not valid");
            throw new IllegalArgumentException("Project cannot be created");
        }
    }

    public ProjectDTO updateProjectById(Long id, ProjectDTO projectDTO) {
        log.info("Updating project {}", projectDTO);
        Project project = projectRepository.findById(id).orElse(null);
        if (isProjectValid(projectDTO)) {
            assert project != null;
            project.setName(projectDTO.getName());
            project.setDescription(projectDTO.getDescription());
            project.setDeadline(projectDTO.getDeadline());
            project.setStatus(projectDTO.getStatus());
            project.setScope(projectDTO.getScope());
            project.setLeader(projectDTO.getLeader());
            project.setMembers(projectDTO.getMembers());
            projectRepository.save(project);
            log.info("Successfully updated project {}", project);
            return convertToDTO(project);
        } else {
            log.info("Project is not valid!");
            throw new IllegalArgumentException("Project cannot be updated");
        }
    }

    public ProjectDTO updateNameById(Long id, String name) {
        log.info("Updating name {}", name);
        Project project = projectRepository.findById(id).orElse(null);
        if (project != null) {
            log.info("Found project updating: {}", project);
            project.setName(name);
            int updated = projectRepository.updateProjectNameById(id, name);
            System.out.println(updated);
            return convertToDTO(project);
        } else {
            throw new IllegalArgumentException("Project is not valid: " + id);
        }
    }

    public ProjectDTO updateDescriptionById(Long id, String description) {
        log.info("Updating description {}", description);
        Project project = projectRepository.findById(id).orElse(null);
        if (project != null) {
            log.info("Found project updating description: {}", project);
            project.setDescription(description);
            int updated = projectRepository.updateProjectDescriptionById(id, description);
            System.out.println(updated);
            return convertToDTO(project);
        } else {
            throw new IllegalArgumentException("Project is not valid: " + id);
        }
    }

    public ProjectDTO updateStatusById(Long id, Status status) {
        log.info("Updating status {}", status);
        Project project = projectRepository.findById(id).orElse(null);
        if (project != null) {
            log.info("Found project updating status: {}", project);
            project.setStatus(status);
            int updated = projectRepository.updateStatusById(id, status);
            System.out.println(updated);
            projectRepository.save(project);
            return convertToDTO(project);
        } else {
            throw new IllegalArgumentException("Project is not valid: " + id);
        }
    }

    public ProjectDTO updateScopeById(Long id, Scope scope) {
        log.info("Updating scope in id {}", id);
        Project project = projectRepository.findById(id).orElse(null);
        if (project != null){
            log.info("Found project, updating scope: {}", scope.getValue());
            project.setScope(scope);
            int updated = projectRepository.updateScopeById(id, scope);
            System.out.println(updated);
            return convertToDTO(project);
        } else {
            throw new IllegalArgumentException("Project is not valid: " + id);
        }
    }

    public ProjectDTO updateDeadLineById(Long id, LocalDate deadline) {
        log.info("Updating deadline in: {}" , id);
        Project project = projectRepository.findById(id).orElse(null);
        if (project != null){
            log.info("Found project, updating deadline: {}", deadline);
            project.setDeadline(deadline);
            int updated = projectRepository.updateDeadlineById(id, deadline);
            System.out.println(updated);
            return convertToDTO(project);
        } else {
            throw new IllegalArgumentException("Project is not valid: " + id);
        }
    }

    public ProjectDTO updateLeaderById(Long id, Long leaderId) {
        log.info("Updating leader in: {}" , id);
        Project project = projectRepository.findById(id).orElse(null);
        User user = userRepository.findById(leaderId).orElse(null);
        if (project != null){
            log.info("Found project, updating leader: {}", user);
            project.setLeader(user);
            int updated = projectRepository.updateLeaderById(id, leaderId);
            System.out.println(updated);
            return convertToDTO(project);
        } else {
            throw new IllegalArgumentException("Project is not valid: " + id);
        }
    }

    public ProjectDTO removeMemberFromProject(Long projectId, Long userId) {
        log.info("Removing member from project {}", projectId);
        Project project = projectRepository.findById(projectId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (project != null){
            log.info("Found project, removing member: {}", user);
            project.getMembers().remove(user);
            int updated = projectRepository.removeMemberFromProject(projectId, userId);
            System.out.println(updated);
            projectRepository.save(project);
            return convertToDTO(project);
        } else {
            throw new IllegalArgumentException("Project is not valid: " + projectId);
        }
    }

    public ProjectDTO addMemberToProject(Long projectId, Long userId) {
        log.info("Adding member to project {}", projectId);
        Project project = projectRepository.findById(projectId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (project != null){
            log.info("Found project, add member: {}", user);
            project.getMembers().add(user);
            int updated = projectRepository.addMemberToProject(projectId, userId);
            System.out.println(updated);
            projectRepository.save(project);
            return convertToDTO(project);
        } else {
            throw new IllegalArgumentException("Project is not valid: " + projectId);
        }
    }

    public ProjectDTO findByName(String name) {
        log.info("Finding project by name {}", name);
        Optional<Project> project = projectRepository.findByName(name);
        if (project.isPresent()){
            log.info("Found project: {}", project.get());
            return convertToDTO(project.get());
        } else {
            throw new IllegalArgumentException("Project is not valid: " + name);
        }
    }

    public ProjectDTO findByLeaderIdAndDescription(Long leaderId, String description) {
        log.info("Finding project by leader id and description {}", leaderId);
        Optional<Project> project = projectRepository.findByLeaderIdAndDescription(leaderId, description);
        if (project.isPresent()){
            log.info("Found project by Leader and Description: {}", project.get());
            return convertToDTO(project.get());
        } else {
            throw new IllegalArgumentException("Project is not valid: " + leaderId);
        }
    }

    public ProjectDTO findByDeadlineIs(LocalDate deadline) {
        log.info("Finding project by deadline {}", deadline);
        Optional<Project> project = projectRepository.findByDeadlineIs(deadline);
        if (project.isPresent()){
            log.info("Found project by deadline: {}" , project.get());
            return convertToDTO(project.get());
        } else {
            throw new IllegalArgumentException("Project is not valid: " + deadline);
        }
    }

    public ProjectDTO findByLeaderIdAndName(Long leaderId, String name) {
        log.info("Finding project by leader id and name {}", leaderId);
        Optional<Project> project = projectRepository.findByLeaderIdAndName(leaderId, name);
        if (project.isPresent()){
            log.info("Found project by Leader and Name: {}", project.get());
            return convertToDTO(project.get());
        } else {
            throw new IllegalArgumentException("Project is not valid: " + leaderId);
        }
    }


}
