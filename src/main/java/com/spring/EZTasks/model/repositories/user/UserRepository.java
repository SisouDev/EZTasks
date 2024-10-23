package com.spring.EZTasks.model.repositories.user;

import com.spring.EZTasks.model.entities.user.User;
import com.spring.EZTasks.utils.enums.Sector;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String username);
    Optional<User> findBySector(Sector sector);
    Optional<User> findBySectorAndName(Sector sector, String name);
    Optional<User> findByRole(String role);
    Optional<User> findBySectorAndRole(Sector sector, String role);
    Optional<User> findByNameAndRole(String name, String role);

    List<User> findAllBySector(Sector sector);
    List<User> findAllBySectorAndRole(Sector sector, String role);
    List<User> findAllByNameAndSector(String name, Sector sector);
    List<User> findAllByRole(String role);
    List<User> findAllByRoleAndSector(String name, Sector sector);
    List<User> findAllByNameAndRole(String name, String role);

    int countBySector(Sector sector);
    int countBySectorAndRole(Sector sector, String role);
    int countByNameAndRole(String name, String role);

    @Modifying
    @Query("UPDATE User u SET u.name = :name WHERE u.id = :id")
    int updateNameById(@Param("id") Long id, @Param("name") String name);

    @Modifying
    @Query("UPDATE User u SET u.sector = :sector WHERE u.id = :id")
    int updateSectorById(Long id, Sector sector);

    @Modifying
    @Query("UPDATE User u SET u.role = :role WHERE u.id = :id")
    int updateRoleById(Long id, String role);

    @Modifying
    @Query("UPDATE User u SET u.email = :email WHERE u.id = :id")
    int updateEmailById(Long id, String email);

    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    int updatePasswordById(Long id, String password);

    void deleteById(@NonNull Long id);

}
