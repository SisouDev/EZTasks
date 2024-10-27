package com.spring.EZTasks.model.repositories.task;

import com.spring.EZTasks.model.entities.task.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTaskId(Long taskId);
    List<Comment> findByTaskIdAndAuthorId(Long taskId, Long authorId);

    int countByTaskId(Long taskId);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.task.id = :taskId")
    void deleteByTaskId(@Param("taskId") Long taskId);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.id = :commentId AND c.task.id = :taskId")
    void deleteByIdAndTaskId(@Param("commentId") Long commentId, @Param("taskId") Long taskId);
}
