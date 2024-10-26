package com.spring.EZTasks.model.repositories.task;

import com.spring.EZTasks.model.entities.task.Tag;
import com.spring.EZTasks.model.entities.task.Task;
import com.spring.EZTasks.model.entities.user.User;
import com.spring.EZTasks.utils.enums.project.Status;
import com.spring.EZTasks.utils.enums.task.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findByTitle(String title);
    Optional<Task> findById(Long id);
    Optional<Task> findByTitleLikeIgnoreCase(String title);
    Optional<Task> findByTitleContainsIgnoreCase(String title);
    Optional<Task> findByDeadlineBetween(LocalDate start, LocalDate end);
    Optional<Task> findByTitleOrDescriptionLikeIgnoreCase(String title, String description);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.tags WHERE t.id = :id")
    Optional<Task> findByIdWithTags(@Param("id") Long id);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.subTasks WHERE t.id = :id")
    Optional<Task> findByIdWithSubTasks(@Param("id") Long id);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.comments WHERE t.id = :id")
    Optional<Task> findByIdWithComments(@Param("id") Long id);

    List<Task> findAllByProjectId(Long projectId);
    List<Task> findAllByDeadlineBetween(LocalDate start, LocalDate end);
    List<Task> findAllByPriority(Priority priority);
    List<Task> findAllByStatus(Status status);
    List<Task> findAllByProjectIdAndDeadlineBetween(Long projectId, LocalDate start, LocalDate end);
    List<Task> findAllByProjectIdAndStatus(Long projectId, Status status);
    List<Task> findAllByProjectIdAndPriority(Long projectId, Priority priority);
    List<Task> findAllByTags(List<Tag> tags);

    int countByProjectId(Long projectId);
    int countByDeadlineBetween(LocalDate start, LocalDate end);
    int countByPriority(Priority priority);
    int countByStatus(Status status);
    int countByProjectIdAndStatus(Long projectId, Status status);
    int countByProjectIdAndPriority(Long projectId, Priority priority);


    @Modifying
    @Query("UPDATE Task t SET t.title = :title WHERE t.id = :taskId")
    int updateTaskTitleById(@Param("taskId") Long taskId, @Param("title") String title);

    @Modifying
    @Query("UPDATE Task t SET t.description = :description WHERE t.id = :taskId")
    int updateTaskDescriptionById(@Param("taskId") Long taskId, @Param("description") String description);

    @Modifying
    @Query("UPDATE Task t SET t.deadline = :deadline WHERE t.id = :taskId")
    int updateTaskDeadlineById(@Param("taskId") Long taskId, @Param("deadline") LocalDate deadline);

    @Modifying
    @Query("UPDATE Task t SET t.priority = :priority WHERE t.id = :taskId")
    int updateTaskPriorityById(@Param("taskId") Long taskId, @Param("priority") Priority priority);

    @Modifying
    @Query("UPDATE Task t SET t.status = :status WHERE t.id = :taskId")
    int updateTaskStatusById(@Param("taskId") Long taskId, @Param("status") Status status);

    @Modifying
    @Query("UPDATE Task t SET t.assignees = :assignees WHERE t.id = :taskId")
    int updateTaskAssigneesById(@Param("taskId") Long taskId, @Param("assignees") List<User> assignees);

    @Modifying
    @Query("UPDATE Task t SET t.tags = :tags WHERE t.id = :taskId")
    int updateTaskTagsById(@Param("taskId") Long taskId, @Param("tags") List<Tag> tags);


    @Modifying
    @Query("UPDATE Task t SET t.tags = FUNCTION('REMOVE_ELEMENT', t.tags, :tag) WHERE t.id = :taskId")
    int removeTagFromTaskById(@Param("taskId") Long taskId, @Param("tag") Tag tag);

}

