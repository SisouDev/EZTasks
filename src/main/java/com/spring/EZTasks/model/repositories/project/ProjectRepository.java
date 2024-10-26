package com.spring.EZTasks.model.repositories.project;

import com.spring.EZTasks.model.entities.project.Project;
import com.spring.EZTasks.utils.enums.project.Scope;
import com.spring.EZTasks.utils.enums.project.Status;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByName(String name);
    Optional<Project> findUserById(Long id);
    Optional<Project> findByLeaderId(Long id);
    Optional<Project> findByLeaderIdAndDescription(Long id, String description);
    Optional<Project> findByLeaderIdAndName(Long id, String name);
    Optional<Project> findByDeadlineIs(LocalDate deadline);

    List<Project> findAllByStatus(Status status);
    List<Project> findAllByMembersId(Long memberId);
    List<Project> findAllByLeaderId(Long id);
    List<Project> findAllByScope(Scope scope);
    List<Project> findAllByStatusAndScope(Status status, Scope scope);
    List<Project> findAllByNameNotContainsIgnoreCase(String name);
    List<Project> findAllByDeadlineAfter(LocalDate date);
    List<Project> findAllByDeadlineBefore(LocalDate date);
    List<Project> findAllByDeadlineBetween(LocalDate start, LocalDate end);
    List<Project> findAllByDescriptionContainingIgnoreCase(String description);


    int countAllByStatus(Status status);
    int countAllByScope(Scope scope);
    int countAllByStatusAndScope(Status status, Scope scope);
    int countAllByLeaderIdIs(Long id);


    @Modifying
    @Transactional
    @Query(
            "UPDATE Project p SET " +
                    "p.name = :name, " +
                    "p.description = :description, " +
                    "p.status = :status, " +
                    "p.deadline = :deadline, " +
                    "p.scope = :scope, " +
                    "p.leader.id = :leaderId " +
                    "WHERE p.id = :id"
    )
    int updateProjectById(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("description") String description,
            @Param("status") Status status,
            @Param("deadline") LocalDate deadline,
            @Param("scope") Scope scope,
            @Param("leaderId") Long leaderId
    );

    @Modifying
    @Transactional
    @Query("UPDATE Project p SET p.name = :name WHERE p.id = :id")
    int updateProjectNameById(@Param("id") Long id, @Param("name") String name);

    @Modifying
    @Transactional
    @Query(
            "UPDATE Project p SET p.description = :description WHERE p.id = :id"
    )
    int updateProjectDescriptionById(@Param("id") Long id, @Param("description") String description);

    @Modifying
    @Transactional
    @Query(
            "UPDATE Project p SET p.status = :status WHERE p.id = :id"
    )
    int updateStatusById(@Param("id") Long id, @Param("status") Status status);

    @Modifying
    @Transactional
    @Query(
            "UPDATE Project p SET p.deadline = :deadline WHERE p.id = :id"
    )
    int updateDeadlineById(@Param("id") Long id, @Param("deadline") LocalDate deadline);

    @Modifying
    @Transactional
    @Query("UPDATE Project p SET p.scope = :scope WHERE p.id = :id")
    int updateScopeById(@Param("id") Long id, @Param("scope") Scope scope);


    @Modifying
    @Transactional
    @Query(
            "UPDATE Project p SET p.leader.id = :leaderId WHERE p.id = :id"
    )
    int updateLeaderById(@Param("id") Long id, @Param("leaderId") Long leaderId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM project_members WHERE project_id = :projectId AND user_id = :memberId", nativeQuery = true)
    int removeMemberFromProject(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO project_members (project_id, user_id) VALUES (:projectId, :memberId)", nativeQuery = true)
    int addMemberToProject(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

}
