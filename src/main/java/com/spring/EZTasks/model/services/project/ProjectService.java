package com.spring.EZTasks.model.services.project;

import com.spring.EZTasks.model.dtos.project.ProjectDTO;
import com.spring.EZTasks.model.entities.project.Project;
import com.spring.EZTasks.model.entities.user.User;
import com.spring.EZTasks.model.repositories.project.ProjectRepository;
import com.spring.EZTasks.model.repositories.user.UserRepository;
import com.spring.EZTasks.utils.enums.project.Scope;
import com.spring.EZTasks.utils.enums.project.Status;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final String separator = "----------------------------------------------------------------";

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
        assert projectDTO != null;
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

    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO, Long leaderId) {
        log.info(separator);
        log.info("Trying to create a new project");
        if (isProjectValid(projectDTO)) {
            User leader = userRepository.findById(leaderId)
                    .orElseThrow(() -> new IllegalArgumentException("Leader not found by id: " + leaderId));
            List<User> members = new ArrayList<>();

            Project project = new Project(
                    projectDTO.getDeadline(),
                    projectDTO.getDescription(),
                    leader,
                    projectDTO.getName(),
                    projectDTO.getScope(),
                    projectDTO.getStatus()
            );
            project.setMembers(members);
            project = projectRepository.save(project);
            log.info("Successfully created project with leaderId {}", project);
            log.info(separator);
            return convertToDTO(project);
        } else {
            log.info("Project is not valid to create");
            throw new IllegalArgumentException("Project cannot be created");
        }
    }

    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        if (isProjectValid(projectDTO)) {
            User leader = userRepository.findById(projectDTO.getLeader().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Leader not found by id: "  + projectDTO.getLeader().getId()));
            List<User> members = new ArrayList<>();
            Project project = new Project(
                    projectDTO.getDeadline(),
                    projectDTO.getDescription(),
                    leader,
                    projectDTO.getName(),
                    projectDTO.getScope(),
                    projectDTO.getStatus()
            );
            project.setMembers(members);
            project = projectRepository.save(project);
            log.info("Successfully created project -> {} ", project);
            return convertToDTO(project);
        } else {
            log.info("Project is not valid");
            throw new IllegalArgumentException("Project cannot be created");
        }
    }

    @Transactional
    public ProjectDTO updateProjectById(Long id, ProjectDTO projectDTO) {
        log.info(separator);
        Project project = projectRepository.findById(id).orElse(null);
        log.info("Old project data{}" , project);
        log.info("New project data {}", projectDTO);
        if (isProjectValid(projectDTO)) {
            if (project == null) {
                throw new IllegalArgumentException("Project not found with id: " + id);
            }
            project.setName(projectDTO.getName());
            project.setDescription(projectDTO.getDescription());
            project.setDeadline(projectDTO.getDeadline());
            project.setStatus(projectDTO.getStatus());
            project.setScope(projectDTO.getScope());
            project.setLeader(projectDTO.getLeader());
            project.setMembers(projectDTO.getMembers());
            projectRepository.save(project);
            log.info("Successfully updated project {}", project);
            log.info(separator);
            return convertToDTO(project);
        } else {
            log.info("Project is not valid!");
            throw new IllegalArgumentException("Project cannot be updated");
        }
    }

    @Transactional
    public ProjectDTO updateNameById(Long id, String name) {
        log.info(separator);
        Project project = projectRepository.findById(id).orElse(null);
        if (project != null) {
            log.info("Old name: {}", project.getName());
            log.info("Found project updating: {}", project);
            project.setName(name);
            int updated = projectRepository.updateProjectNameById(id, name);
            log.info("Updating name: {}", name);
            System.out.println(updated);
            log.info(separator);
            return convertToDTO(project);
        } else {
            throw new IllegalArgumentException("Project is not valid: " + id);
        }
    }

    @Transactional
    public ProjectDTO updateDescriptionById(Long id, String description) {
        log.info(separator);
        log.info("Updating description {}", description);
        Project project = projectRepository.findById(id).orElse(null);
        if (project != null) {
            log.info("Found project updating description: {}", project);
            project.setDescription(description);
            int updated = projectRepository.updateProjectDescriptionById(id, description);
            System.out.println(updated);
            log.info(separator);
            return convertToDTO(project);
        } else {
            throw new IllegalArgumentException("Project is not valid: " + id);
        }
    }

    @Transactional
    public ProjectDTO updateStatusById(Long id, Status status) {
        log.info(separator);
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

    @Transactional
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

    @Transactional
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

    @Transactional
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

    @Transactional
    public ProjectDTO removeMemberFromProject(Long projectId, Long userId) {
        log.info("Removing member from project {}", projectId);
        Project project = projectRepository.findById(projectId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (project != null){
            log.info("Project name to remove: {}", project.getName());
            log.info("Leader name : {}", project.getLeader().getName());
            log.info("Found project, removing member: {}", user.getName());
            project.getMembers().remove(user);
            int updated = projectRepository.removeMemberFromProject(projectId, userId);
            System.out.println(updated);
            projectRepository.save(project);
            return convertToDTO(project);
        } else {
            throw new IllegalArgumentException("Project is not valid: " + projectId);
        }
    }

    @Transactional
    public ProjectDTO addMemberToProject(Long projectId, Long userId) {
        log.info(separator);
        log.info("Adding member to project {}", projectId);
        Project project = projectRepository.findById(projectId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (project != null){
            log.info("Project name: {}", project.getName());
            log.info("Leader name: {}", project.getLeader().getName());
            log.info("Found project, add member: {}", user.getName());
            project.getMembers().add(user);
            int updated = projectRepository.addMemberToProject(projectId, userId);
            System.out.println(updated);
            projectRepository.save(project);
            log.info(separator);
            return convertToDTO(project);
        } else {
            throw new IllegalArgumentException("Project is not valid: " + projectId);
        }
    }

    public ProjectDTO findById(Long id){
        log.info("-------------------------------------------");
        log.info("Finding project by id {}", id);
        Project project = projectRepository.findById(id).orElse(null);
        log.info("Project data returned by id: {}", project);
        return convertToDTO(project);
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

    public List<ProjectDTO> findAll() {
        log.info(separator);
        log.info("Finding all projects");
        List<Project> projects = projectRepository.findAll();
        log.info("Found {} projects, size", projects.size());
        log.info(separator);
        return projects
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> findAllByStatus(Status status) {
        log.info(separator);
        log.info("Finding all projects by status {}", status);
        List<Project> projects = projectRepository.findAllByStatus(status);
        log.info("Found {} projects: ", projects.size());
        log.info(separator);
        return projects
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> findAllByLeaderId(Long leaderId) {
        log.info(separator);
        log.info("Finding all projects by leader id {}", leaderId);
        List<Project> projects = projectRepository.findAllByLeaderId(leaderId);
        log.info("Found {} projects with leaderid: ", projects.size());
        log.info(separator);
        return projects
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> findAllByScope(Scope scope) {
        log.info("Finding all projects by scope {}", scope);
        List<Project> projects = projectRepository.findAllByScope(scope);
        return projects
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> findAllByDeadlineAfter(LocalDate date) {
        log.info("Finding all projects with deadline after {}", date);
        List<Project> projects = projectRepository.findAllByDeadlineAfter(date);
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<Project> findAllProjectsByMemberId(Long memberId){
        log.info("Finding all projects where the user with: {} is member", memberId);
        List<Project> projects = projectRepository.findAllByMembersId(memberId);
        log.info("Found {} projects", projects.size());
        log.info("Projects names {}", projects.stream().map(Project::getName).collect(Collectors.toList()));
        return projects;
    }

    public List<ProjectDTO> findAllByDeadlineBefore(LocalDate date) {
        log.info("Finding all projects with deadline before {}", date);
        List<Project> projects = projectRepository.findAllByDeadlineBefore(date);
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ProjectDTO> findAllByDeadlineBetween(LocalDate start, LocalDate end) {
        log.info("Finding all projects with deadline between {} and {}", start, end);
        List<Project> projects = projectRepository.findAllByDeadlineBetween(start, end);
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ProjectDTO> findAllByDescriptionContaining(String description) {
        log.info("Finding all projects containing description '{}'", description);
        List<Project> projects = projectRepository.findAllByDescriptionContainingIgnoreCase(description);
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ProjectDTO> findAllByNameNotContaining(String name) {
        log.info("Finding all projects with name not containing '{}'", name);
        List<Project> projects = projectRepository.findAllByNameNotContainsIgnoreCase(name);
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public int countProjectsByStatus(Status status) {
        log.info("Counting projects by status {}", status);
        return projectRepository.countAllByStatus(status);
    }

    public int countProjectsByScope(Scope scope) {
        log.info("Counting projects by scope {}", scope);
        return projectRepository.countAllByScope(scope);
    }

    public int countProjectsByStatusAndScope(Status status, Scope scope) {
        log.info("Counting projects by status {} and scope {}", status, scope);
        return projectRepository.countAllByStatusAndScope(status, scope);
    }

    public int countProjectsByLeaderId(Long leaderId) {
        log.info("Counting projects by leader ID {}", leaderId);
        return projectRepository.countAllByLeaderIdIs(leaderId);
    }

    public void deleteProjectById(Long id) {
        log.info(separator);
        log.info("Deleting project with ID {}", id);
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            log.info("Successfully deleted project with ID {}", id);
            log.info(separator);
        } else {
            throw new IllegalArgumentException("Project not found with id: " + id);
        }
    }


}
