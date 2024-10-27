package com.spring.EZTasks.model.repositories.task;

import com.spring.EZTasks.model.entities.task.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByNameIgnoreCase(String name);

    @Query("SELECT t FROM Tag t JOIN t.tasks task WHERE task.id = :taskId")
    List<Tag> findAllByTaskId(@Param("taskId") Long taskId);

    @Query("SELECT COUNT(task) FROM Task task JOIN task.tags tag WHERE tag.id = :tagId")
    int countTasksByTagId(@Param("tagId") Long tagId);


}
