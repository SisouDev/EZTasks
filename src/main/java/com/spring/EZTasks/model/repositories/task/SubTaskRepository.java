package com.spring.EZTasks.model.repositories.task;

import com.spring.EZTasks.model.entities.task.SubTask;
import com.spring.EZTasks.utils.enums.project.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubTaskRepository extends JpaRepository <SubTask, Long> {
    List<SubTask> findAllByTaskId(Long id);
    List<SubTask> findAllByTaskIdAndStatus(Long id, Status status);

    int countAllByTaskIdAndStatus(Long id, Status status);

    @Modifying
    @Query("DELETE FROM SubTask s WHERE s.task.id = :taskId")
    void deleteByTaskId(@Param("taskId") Long taskId);


}
