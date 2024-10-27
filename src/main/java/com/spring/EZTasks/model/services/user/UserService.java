package com.spring.EZTasks.model.services.user;

import com.spring.EZTasks.model.dtos.user.UserDTO;
import com.spring.EZTasks.model.entities.user.User;
import com.spring.EZTasks.model.repositories.user.UserRepository;
import com.spring.EZTasks.utils.enums.user.Sector;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserDTO convertToDTO(User user) {
        if (user == null) {
            log.info("User is null");
            throw new EntityNotFoundException("User not found");
        }
        log.info("Converting UserDTO to UserDTO");
        log.info(user.toString());
        log.info(String.valueOf(user.getId()));
        log.info(user.getName());
        log.info(user.getPassword());
        log.info(user.getEmail());
        log.info(user.getRole());
        log.info(user.getSector().toString());
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getSector()
        );
    }

    private User convertToEntity(UserDTO userDTO) {
        if (userDTO.getId() != null) {
            log.info(userDTO.getId().toString());
        }
        log.info(userDTO.toString());
        log.info(userDTO.getEmail());
        log.info(userDTO.getPassword());
        log.info(userDTO.getRole());
        log.info(userDTO.getSector().toString());
        log.info(userDTO.getName());

        return new User(
                userDTO.getEmail(),
                userDTO.getName(),
                userDTO.getPassword(),
                userDTO.getRole(),
                userDTO.getSector()
        );
    }

    public boolean isValidUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        log.info("UserDTO {}", userDTO.toString());
        if(user.getPassword() == null || user.getPassword().length() < 6) {
            log.info("Password is too short");
            return false;
        }else if(user.getName() == null || user.getName().length() < 3) {
            log.info("Name is too short");
            return false;
        }else if(user.getEmail() == null || user.getEmail().length() < 3) {
            log.info("Email is too short");
            return false;
        } else if(user.getRole() == null || user.getRole().length() < 3) {
            log.info("Role is too short");
            return false;
        } else if(user.getSector() == null) {
            log.info("Sector is too short");
            return false;
        }
        log.info("Is valid!");
        return true;
    }

    public UserDTO create(UserDTO userDTO) {
        if (isValidUser(userDTO)) {
            User user = convertToEntity(userDTO);
            user = userRepository.save(user);
            log.info("Saved user: {}", user);
            return convertToDTO(user);
        } else {
            throw new IllegalArgumentException("Invalid user");
        }
    }

    public List<UserDTO> createUsers(List<UserDTO> userDTOList) {
        List<User> users = userDTOList.stream()
                .filter(this::isValidUser)
                .map(this::convertToEntity)
                .toList();

        if (users.isEmpty()) {
            throw new IllegalArgumentException("No users found");
        }

        List<User> savedUsers = userRepository.saveAll(users);
        return savedUsers.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<UserDTO> findAll() {
        log.info("Finding all users");
        List<User> users = userRepository.findAll();
        log.info("Found {} users", users.size());
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> findById(Long id) {
        log.info("Finding user by id : {}", id);
        Optional<User> user = userRepository.findById(id);
        log.info("Founded user : {}", user);
        return user.map(this::convertToDTO);
    }

    public Optional<UserDTO> findByEmail(String email) {
        log.info("Finding user by email: {}", email);
        Optional<User> user = userRepository.findByEmail(email);
        log.info("Found user: {}", user);
        return user.map(this::convertToDTO);
    }

    public Optional<UserDTO> findByName(String name) {
        log.info("Finding user by name: {}", name);
        Optional<User> user = userRepository.findByName(name);
        log.info("Found user by name: {}", user);
        return user.map(this::convertToDTO);
    }

    public Optional<UserDTO> findBySector(Sector sector) {
        log.info("Finding user by sector: {}", sector.getSector());
        Optional<User> user = userRepository.findBySector(sector);
        log.info("Found user by sector: {}", user);
        return user.map(this::convertToDTO);
    }

    public Optional<UserDTO> findBySectorAndName(Sector sector, String name) {
        log.info("Finding user by sector and name: {}", sector.getSector());
        Optional<User> user = userRepository.findBySectorAndName(sector, name);
        log.info("Found user by sector and name: {}", user);
        return user.map(this::convertToDTO);
    }

    public Optional<UserDTO> findByRole(String role) {
        log.info("Finding user by role: {}", role);
        Optional<User> user = userRepository.findByRole(role);
        log.info("Found user by role: {}", user);
        return user.map(this::convertToDTO);
    }

    public Optional<UserDTO> findBySectorAndRole(Sector sector, String role) {
        log.info("Finding user by sector and role: {}", sector.getSector());
        Optional<User> user = userRepository.findBySectorAndRole(sector, role);
        log.info("Found user by sector and role: {}", user);
        return user.map(this::convertToDTO);
    }

    public Optional<UserDTO> findByNameAndRole(String name, String role) {
        log.info("Finding user by name: {} and role: {}", name, role);
        Optional<User> user = userRepository.findByNameAndRole(name, role);
        log.info("Found user by name: {} and role: {}", user, role);
        return user.map(this::convertToDTO);
    }

    public List<UserDTO> findAllBySector(Sector sector) {
        log.info("Finding all users by sector: {}", sector.getSector());
        List<User> users = userRepository.findAllBySector(sector);
        log.info("Found {} users ", users.size());
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> findAllBySectorAndRole(Sector sector, String role) {
        log.info("Finding all users by sector and role: {}", sector.getSector());
        List<User> users = userRepository.findAllBySectorAndRole(sector, role);
        log.info("Found {} users:", users.size());
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> findAllByNameAndSector(String name, Sector sector) {
        log.info("Finding all users by sector and name: {}", sector.getSector());
        List<User> users = userRepository.findAllByNameAndSector(name, sector);
        log.info("Found {} users - ", users.size());
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> findAllByRole(String role) {
        log.info("Finding all users by role: {}", role);
        List<User> users = userRepository.findAllByRole(role);
        log.info("Found {} users ->", users.size());
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> findAllByRoleAndSector(String name, Sector sector) {
        log.info("Finding all users by role and name: {}", sector.getSector());
        List<User> users = userRepository.findAllByRoleAndSector(name, sector);
        log.info("Found {} users -->", users.size());
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> findAllByNameAndRole(String name, String role) {
        log.info("Finding all users by name: {} and role: {}", name, role);
        List<User> users = userRepository.findAllByNameAndRole(name, role);
        log.info("Found {} users >", users.size());
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public int countBySector(Sector sector) {
        log.info("Counting users by sector: {}", sector.getSector());
        return userRepository.countBySector(sector);
    }

    public int countBySectorAndRole(Sector sector, String role) {
        log.info("Counting users by sector and role: {}", sector.getSector());
        return userRepository.countBySectorAndRole(sector, role);
    }

    public int countByNameAndRole(String name, String role) {
        log.info("Counting users by name: {} and role: {}", name, role);
        return userRepository.countByNameAndRole(name, role);
    }

    public UserDTO getUserById(Long id) {
        log.info("Finding user by id: {}", id);
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            log.info("Found user : {}", user);
            return convertToDTO(user);
        }else {
            log.info("User not found");
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.info("Updating user with id: {}", id);
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            log.info("Found user -> {}", user);
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setRole(userDTO.getRole());
            user.setSector(userDTO.getSector());
            userRepository.save(user);
            return convertToDTO(user);
        } else {
            log.info("User not found!");
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            log.info("Found user deleting {}", user);
            userRepository.deleteById(id);
        }else {
            log.info("User not found by id.");
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    public UserDTO updateNameById(Long id, String name) {
        log.info("Updating name user with id: {}", id);
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            log.info("Found user updating {}", user);
            user.setName(name);
            int updated = userRepository.updateNameById(id, name);
            System.out.println(updated);
            return convertToDTO(user);
        } else {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    public UserDTO updateEmailById(Long id, String email) {
        log.info("Updating email user with id: {}", id);
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            log.info("Found user email updating {}", user);
            user.setEmail(email);
            int updated = userRepository.updateEmailById(id, email);
            System.out.println(updated);
            return convertToDTO(user);
        } else {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    public UserDTO updateSectorById(Long id, Sector sector) {
        log.info("Updating sector user with id: {}", id);
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            log.info("Found user sector updating {}", user);
            user.setSector(sector);
            int updated = userRepository.updateSectorById(id, sector);
            System.out.println(updated);
            return convertToDTO(user);
        }else {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    public UserDTO updateRoleById(Long id, String role) {
        log.info("Updating role user with id: {}", id);
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            log.info("Found user role updating {}", user);
            user.setRole(role);
            int updated = userRepository.updateRoleById(id, role);
            System.out.println(updated);
            return convertToDTO(user);
        } else {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    public UserDTO updatePasswordById(Long id, String password) {
        log.info("Updating password user with id: {}", id);
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            log.info("Found user password updating {}", user);
            user.setPassword(password);
            int updated = userRepository.updatePasswordById(id, password);
            System.out.println(updated);
            return convertToDTO(user);
        } else {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

}

