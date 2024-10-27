package com.spring.EZTasks.model.services.task;

import com.spring.EZTasks.exceptions.ResourceNotFoundException;
import com.spring.EZTasks.model.dtos.task.TaskDTO;
import com.spring.EZTasks.model.entities.project.Project;
import com.spring.EZTasks.model.entities.task.Comment;
import com.spring.EZTasks.model.entities.task.SubTask;
import com.spring.EZTasks.model.entities.task.Tag;
import com.spring.EZTasks.model.entities.task.Task;
import com.spring.EZTasks.model.entities.user.User;
import com.spring.EZTasks.model.repositories.project.ProjectRepository;
import com.spring.EZTasks.model.repositories.task.CommentRepository;
import com.spring.EZTasks.model.repositories.task.SubTaskRepository;
import com.spring.EZTasks.model.repositories.task.TagRepository;
import com.spring.EZTasks.model.repositories.task.TaskRepository;
import com.spring.EZTasks.model.repositories.user.UserRepository;
import com.spring.EZTasks.utils.enums.project.Status;
import com.spring.EZTasks.utils.enums.task.Priority;
import com.spring.EZTasks.utils.func.LogHelper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final SubTaskRepository subTaskRepository;
    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final LogHelper logHelper;

    @Autowired
    public TaskService(
            TaskRepository taskRepository, SubTaskRepository subTaskRepository, CommentRepository commentRepository,
            TagRepository tagRepository, UserRepository userRepository, ProjectRepository projectRepository, LogHelper logHelper, LogHelper logHelper1
    ){
        this.taskRepository = taskRepository;
        this.subTaskRepository = subTaskRepository;
        this.commentRepository = commentRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.logHelper = logHelper1;
    }

    public TaskDTO convertToDTO(Task task) {
        logHelper.logSeparator();
        logHelper.logSection("Convert to DTO");
        logHelper.logMessage("Conversion", "Init a trying conversion of: " + task);
        log.info("Convert to TaskDTO");
        if (task == null) {
            logHelper.logError("Convert to TaskDTO", "Task is null");
            log.info("Task is null");
            throw new ResourceNotFoundException("Task is null");
        }
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDeadline(),
                task.getStatus(),
                task.getPriority(),
                task.getProject(),
                task.getAssignees(),
                task.getTags(),
                task.getSubTasks(),
                task.getComments()
        );
    }

    public Task convertToEntity(TaskDTO taskDTO) {
        logHelper.logSeparator();
        logHelper.logSection("Convert to Entity");
        logHelper.logMessage("Conversion", "Init a trying conversion of: " + taskDTO);
        log.info("Convert to Task");
        if (taskDTO == null) {
            logHelper.logError("Convert to TaskDTO", "TaskDTO is null");
            log.info("TaskDto is null");
            throw new ResourceNotFoundException("Task is null");
        }
        return new Task(
                taskDTO.getId(),
                taskDTO.getTitle(),
                taskDTO.getDescription(),
                taskDTO.getStatus(),
                taskDTO.getProject(),
                taskDTO.getPriority(),
                taskDTO.getDeadline(),
                taskDTO.getAssignees(),
                taskDTO.getTags()
        );
    }

    public boolean isTaskValid(TaskDTO taskDTO) {
        logHelper.logSeparator();
        logHelper.logSection("Task Validation");
        logHelper.logMessage("Validation", "Validating TaskDTO: " + taskDTO);
        if (taskDTO.getTitle().length() < 2 || taskDTO.getDescription().length() < 2) {
            logHelper.logError("Validation", "Title or description is too short.");
            return false;
        }
        if (taskDTO.getProject() == null || taskDTO.getPriority() == null || taskDTO.getStatus() == null) {
            logHelper.logError("Validation", "Project, priority, or status is missing.");
            return false;
        }
        if (taskDTO.getDeadline() == null) {
            logHelper.logError("Validation", "Deadline is missing.");
            return false;
        }
        logHelper.logMessage("Validation", "TaskDTO is valid");
        return true;
    }

    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO, Long projectId, List<Long> assigneesIds, List<Long> tagIds, List<Long> subTaskIds, List<Long> commentIds) {
        logHelper.logSeparator();
        logHelper.logSection("Creating new Task");
        logHelper.logMessage("Create", "Trying to create a new Task: " + taskDTO);

        if (projectId == null) {
            logHelper.logError("Create", "Project id is null");
            throw new ResourceNotFoundException("Project id is required");
        }

        if (tagIds == null || tagIds.isEmpty()) {
            logHelper.logError("Create", "No tags provided");
            throw new ResourceNotFoundException("At least one tag is required");
        }

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        taskDTO.setProject(project);

        List<User> assignees = new ArrayList<>();
        for (Long assigneeId : assigneesIds) {
            User assignee = userRepository.findById(assigneeId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            assignees.add(assignee);
        }
        taskDTO.setAssignees(assignees);

        List<Tag> tags = new ArrayList<>();
        for (Long tagId : tagIds) {
            Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
            tags.add(tag);
        }
        taskDTO.setTags(tags);

        if (isTaskValid(taskDTO)) {
            List<SubTask> subTasks = new ArrayList<>();
            if (subTaskIds != null) {
                for (Long subTaskId : subTaskIds) {
                    SubTask subTask = subTaskRepository.findById(subTaskId).orElseThrow(() -> new ResourceNotFoundException("SubTask not found"));
                    subTasks.add(subTask);
                }
            }
            taskDTO.setSubTasks(subTasks);

            List<Comment> comments = new ArrayList<>();
            if (commentIds != null) {
                for (Long commentId : commentIds) {
                    Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
                    comments.add(comment);
                }
            }
            taskDTO.setComments(comments);

            Task task = convertToEntity(taskDTO);
            task = taskRepository.save(task);

            logHelper.logMessage("Create", "Successfully created a new Task: " + taskDTO);
            logHelper.logSeparator();
            return convertToDTO(task);
        } else {
            logHelper.logError("Create", "Task data is not valid");
            throw new ResourceNotFoundException("Task data is not valid");
        }
    }


    public Optional<Task> findByIdWithDetails(Long taskId) {
        logHelper.logSeparator();
        logHelper.logSection("Finding Task with Details");
        logHelper.logMessage("Find", "Fetching Task details for Task ID: " + taskId);

        Optional<Task> task = taskRepository.findByIdWithTags(taskId);
        task.ifPresent(t -> {
            t.setSubTasks(subTaskRepository.findAllByTaskId(taskId));
            t.setComments(commentRepository.findByTaskId(taskId));
            logHelper.logMessage("Find", "Successfully retrieved Task details with SubTasks and Comments for Task ID: " + taskId);
        });

        return task;
    }

    @Transactional
    public TaskDTO updateTaskTitle(Long taskId, String newTitle) {
        log.info("Updating Task title for taskId: {}, newTitle: {}", taskId, newTitle);

        if (newTitle == null || newTitle.length() < 2) {
            throw new IllegalArgumentException("Title is invalid");
        }

        int updatedRows = taskRepository.updateTaskTitleById(taskId, newTitle);
        if (updatedRows > 0) {
            return convertToDTO(taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found")));
        } else {
            throw new ResourceNotFoundException("Task not found");
        }
    }

    @Transactional
    public TaskDTO updateTaskDescription(Long taskId, String newDescription) {
        log.info("Starting updateTaskDescription for taskId: {}, newDescription: {}", taskId, newDescription);

        if (newDescription == null || newDescription.length() < 2) {
            log.warn("Invalid description provided for taskId: {}", taskId);
            throw new IllegalArgumentException("Description is invalid");
        }

        int updatedRows = taskRepository.updateTaskDescriptionById(taskId, newDescription);
        if (updatedRows > 0) {
            log.info("Description updated successfully for taskId: {}", taskId);
            return convertToDTO(taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found")));
        } else {
            log.warn("Task not found for updateTaskDescription with taskId: {}", taskId);
            throw new ResourceNotFoundException("Task not found");
        }
    }

    @Transactional
    public TaskDTO updateTaskDeadline(Long taskId, LocalDate newDeadline) {
        log.info("Starting updateTaskDeadline for taskId: {}, newDeadline: {}", taskId, newDeadline);

        if (newDeadline == null) {
            log.warn("Invalid deadline provided for taskId: {}", taskId);
            throw new IllegalArgumentException("Deadline is invalid");
        }

        int updatedRows = taskRepository.updateTaskDeadlineById(taskId, newDeadline);
        if (updatedRows > 0) {
            log.info("Deadline updated successfully for taskId: {}", taskId);
            return convertToDTO(taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found")));
        } else {
            log.warn("Task not found for updateTaskDeadline with taskId: {}", taskId);
            throw new ResourceNotFoundException("Task not found");
        }
    }

    @Transactional
    public TaskDTO updateTaskPriority(Long taskId, Priority newPriority) {
        log.info("Starting updateTaskPriority for taskId: {}, newPriority: {}", taskId, newPriority);

        if (newPriority == null) {
            log.warn("Invalid priority provided for taskId: {}", taskId);
            throw new IllegalArgumentException("Priority is invalid");
        }

        int updatedRows = taskRepository.updateTaskPriorityById(taskId, newPriority);
        if (updatedRows > 0) {
            log.info("Priority updated successfully for taskId: {}", taskId);
            return convertToDTO(taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found")));
        } else {
            log.warn("Task not found for updateTaskPriority with taskId: {}", taskId);
            throw new ResourceNotFoundException("Task not found");
        }
    }

    @Transactional
    public TaskDTO updateTaskStatus(Long taskId, Status newStatus) {
        log.info("Starting updateTaskStatus for taskId: {}, newStatus: {}", taskId, newStatus);

        if (newStatus == null) {
            log.warn("Invalid status provided for taskId: {}", taskId);
            throw new IllegalArgumentException("Status is invalid");
        }

        int updatedRows = taskRepository.updateTaskStatusById(taskId, newStatus);
        if (updatedRows > 0) {
            log.info("Status updated successfully for taskId: {}", taskId);
            return convertToDTO(taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found")));
        } else {
            log.warn("Task not found for updateTaskStatus with taskId: {}", taskId);
            throw new ResourceNotFoundException("Task not found");
        }
    }

    @Transactional
    public TaskDTO updateTaskAssignees(Long taskId, List<Long> assigneeIds) {
        log.info("Starting updateTaskAssignees for taskId: {}", taskId);

        List<User> assignees = new ArrayList<>();
        for (Long assigneeId : assigneeIds) {
            log.info("Fetching user with ID: {}", assigneeId);
            User assignee = userRepository.findById(assigneeId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            assignees.add(assignee);
        }

        int updatedRows = taskRepository.updateTaskAssigneesById(taskId, assignees);
        if (updatedRows > 0) {
            log.info("Assignees updated successfully for taskId: {}", taskId);
            return convertToDTO(taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found")));
        } else {
            log.warn("Task not found for updateTaskAssignees with taskId: {}", taskId);
            throw new ResourceNotFoundException("Task not found");
        }
    }

    @Transactional
    public TaskDTO updateTaskTags(Long taskId, List<Long> tagIds) {
        log.info("Starting updateTaskTags for taskId: {}", taskId);

        List<Tag> tags = new ArrayList<>();
        for (Long tagId : tagIds) {
            log.info("Fetching tag with ID: {}", tagId);
            Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
            tags.add(tag);
        }

        int updatedRows = taskRepository.updateTaskTagsById(taskId, tags);
        if (updatedRows > 0) {
            log.info("Tags updated successfully for taskId: {}", taskId);
            return convertToDTO(taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found")));
        } else {
            log.warn("Task not found for updateTaskTags with taskId: {}", taskId);
            throw new ResourceNotFoundException("Task not found");
        }
    }

    @Transactional
    public TaskDTO removeTagFromTask(Long taskId, Long tagId) {
        log.info("Starting removeTagFromTask for taskId: {}, tagId: {}", taskId, tagId);

        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        int updatedRows = taskRepository.removeTagFromTaskById(taskId, tag);

        if (updatedRows > 0) {
            log.info("Tag removed successfully from taskId: {}", taskId);
            return convertToDTO(taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found")));
        } else {
            log.warn("Task not found or tag not associated with task for taskId: {}, tagId: {}", taskId, tagId);
            throw new ResourceNotFoundException("Task not found or tag not associated with task");
        }
    }

    @Transactional
    public TaskDTO addCommentToTask(Long taskId, Comment comment, Long authorId) {
        log.info("Starting addCommentToTask for taskId: {}", taskId);
        Task task = taskRepository.findByIdWithComments(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));
        comment.setAuthor(author);

        comment.setTask(task);
        commentRepository.save(comment);

        task.getComments().add(comment);
        return convertToDTO(task);
    }

    @Transactional
    public TaskDTO addTagToTask(Long taskId, Long tagId) {
        log.info("Starting addTagToTask for taskId: {}", taskId);
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        Task task = taskRepository.findByIdWithTags(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        task.getTags().add(tag);
        taskRepository.save(task);
        return convertToDTO(task);
    }

    @Transactional
    public TaskDTO addSubtaskToTask(Long taskId, SubTask subTask) {
        log.info("Starting addSubtaskToTask for taskId: {}", taskId);
        Task task = taskRepository.findByIdWithSubTasks(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        subTask.setTask(task);
        subTaskRepository.save(subTask);

        task.getSubTasks().add(subTask);
        taskRepository.save(task);
        return convertToDTO(task);
    }

    @Transactional
    public TaskDTO removeCommentFromTask(Long taskId, Long commentId) {
        log.info("Starting removeCommentFromTask for taskId: {}", taskId);
        Task task = taskRepository.findByIdWithComments(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        task.getComments().remove(comment);
        commentRepository.delete(comment);

        return convertToDTO(task);
    }

    @Transactional
    public TaskDTO removeTagToTask(Long taskId, Long tagId) {
        log.info("Starting removeTagToTask for taskId: {}", taskId);
        Task task = taskRepository.findByIdWithTags(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));

        task.getTags().remove(tag);
        taskRepository.save(task);

        return convertToDTO(task);
    }

    @Transactional
    public TaskDTO removeSubtaskToTask(Long taskId, Long subTaskId) {
        log.info("Starting removeSubtaskToTask for taskId: {}", taskId);
        Task task = taskRepository.findByIdWithSubTasks(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        SubTask subTask = subTaskRepository.findById(subTaskId)
                .orElseThrow(() -> new ResourceNotFoundException("SubTask not found"));

        task.getSubTasks().remove(subTask);
        subTaskRepository.delete(subTask);

        return convertToDTO(task);
    }

    public List<TaskDTO> findAllByProjectId(Long projectId) {
        logHelper.logMessage("Find All", "Finding all by project id");
        List<Task> tasks = taskRepository.findAllByProjectId(projectId);
        return tasks.stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<TaskDTO> findAllByDeadlineBetween(LocalDate startDate, LocalDate endDate) {
        logHelper.logMessage("Find All", "Finding all by deadline between");
        List<Task> tasks = taskRepository.findAllByDeadlineBetween(startDate, endDate);
        return tasks.stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<TaskDTO> findAllByPriority(Priority priority) {
        logHelper.logMessage("Find All", "Finding all by priority");
        List<Task> tasks = taskRepository.findAllByPriority(priority);
        return tasks.stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<TaskDTO> findAllByStatus(Status status){
        logHelper.logMessage("Find All", "Finding all by status");
        List<Task> tasks = taskRepository.findAllByStatus(status);
        return tasks.stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<TaskDTO> findByTitleLikeIgnoreCase(String title) {
        logHelper.logMessage("Find All", "Finding by title like");
        Optional<Task> task = taskRepository.findByTitleLikeIgnoreCase(title);
        return task.map(this::convertToDTO);
    }


}
