package com.spring.EZTasks.controller.task;

import com.spring.EZTasks.exceptions.ResourceNotFoundException;
import com.spring.EZTasks.model.dtos.task.TaskDTO;
import com.spring.EZTasks.model.entities.task.Comment;
import com.spring.EZTasks.model.entities.task.SubTask;
import com.spring.EZTasks.model.entities.task.Task;
import com.spring.EZTasks.model.services.task.TaskService;
import com.spring.EZTasks.utils.enums.project.Status;
import com.spring.EZTasks.utils.enums.task.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO,
                                              @RequestParam Long projectId,
                                              @RequestParam List<Long> assigneesIds,
                                              @RequestParam List<Long> tagIds,
                                              @RequestParam(required = false) List<Long> subTaskIds,
                                              @RequestParam(required = false) List<Long> commentIds) {
        TaskDTO createdTask = taskService.createTask(taskDTO, projectId, assigneesIds, tagIds, subTaskIds, commentIds);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PostMapping("/{taskId}/subtasks")
    public ResponseEntity<TaskDTO> addSubtaskToTask(@PathVariable Long taskId, @RequestBody SubTask subTask) {
        TaskDTO updatedTask = taskService.addSubtaskToTask(taskId, subTask);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
       Optional<Task> task = taskService.findByIdWithDetails(id);
        return task.map(value -> ResponseEntity.ok(taskService.convertToDTO(value)))
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    @PutMapping("/{id}/title")
    public ResponseEntity<TaskDTO> updateTaskTitle(@PathVariable Long id, @RequestParam String newTitle) {
        TaskDTO updatedTask = taskService.updateTaskTitle(id, newTitle);
        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/{id}/description")
    public ResponseEntity<TaskDTO> updateTaskDescription(@PathVariable Long id, @RequestParam String newDescription) {
        TaskDTO updatedTask = taskService.updateTaskDescription(id, newDescription);
        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/{id}/deadline")
    public ResponseEntity<TaskDTO> updateTaskDeadline(@PathVariable Long id, @RequestParam LocalDate newDeadline) {
        TaskDTO updatedTask = taskService.updateTaskDeadline(id, newDeadline);
        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/{id}/priority")
    public ResponseEntity<TaskDTO> updateTaskPriority(@PathVariable Long id, @RequestParam Priority newPriority) {
        TaskDTO updatedTask = taskService.updateTaskPriority(id, newPriority);
        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(@PathVariable Long id, @RequestParam Status newStatus) {
        TaskDTO updatedTask = taskService.updateTaskStatus(id, newStatus);
        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/{id}/assignees")
    public ResponseEntity<TaskDTO> updateTaskAssignees(@PathVariable Long id, @RequestParam List<Long> assigneeIds) {
        TaskDTO updatedTask = taskService.updateTaskAssignees(id, assigneeIds);
        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/{id}/tags")
    public ResponseEntity<TaskDTO> updateTaskTags(@PathVariable Long id, @RequestParam List<Long> tagIds) {
        TaskDTO updatedTask = taskService.updateTaskTags(id, tagIds);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}/tags/{tagId}")
    public ResponseEntity<TaskDTO> removeTagFromTask(@PathVariable Long id, @PathVariable Long tagId) {
        TaskDTO updatedTask = taskService.removeTagFromTask(id, tagId);
        return ResponseEntity.ok(updatedTask);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<TaskDTO> addCommentToTask(@PathVariable Long id, @RequestBody Comment comment) {
        TaskDTO updatedTask = taskService.addCommentToTask(id, comment);
        return ResponseEntity.ok(updatedTask);
    }

    @PostMapping("/{id}/tags/{tagId}")
    public ResponseEntity<TaskDTO> addTagToTask(@PathVariable Long id, @PathVariable Long tagId) {
        TaskDTO updatedTask = taskService.addTagToTask(id, tagId);
        return ResponseEntity.ok(updatedTask);
    }
}
