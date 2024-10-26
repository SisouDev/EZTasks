package com.spring.EZTasks.config;

import com.spring.EZTasks.model.entities.project.Project;
import com.spring.EZTasks.model.entities.user.User;
import com.spring.EZTasks.model.repositories.project.ProjectRepository;
import com.spring.EZTasks.model.repositories.user.UserRepository;
import com.spring.EZTasks.utils.enums.project.Scope;
import com.spring.EZTasks.utils.enums.project.Status;
import com.spring.EZTasks.utils.enums.user.Sector;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.Arrays;

@Configuration
@Profile("test")
public class DataSeed {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, ProjectRepository projectRepository) {
        return args -> {
            userRepository.deleteAll();
            User user1 = new User("gabriel@gmail.com", "Gabriel", "senha123", "Engenheiro", Sector.ENG);
            User user2 = new User("jorge@gmail.com", "Jorge Santos", "@senha321", "Analista", Sector.MKT);
            User user3 = new User("anelisa@ig.com", "Anelisa", "@senha22", "Analista", Sector.COM);
            User user4 = new User("joao@ig.com", "Joao Pedro", "password123", "Engenheiro", Sector.IT);
            User user5 = new User("tiago@gmail.com", "Tiago Paulo", "@password321", "Analista", Sector.MKT);
            User user6 = new User("afonso@ig.com", "Afonso Luis", "password123", "Engenheiro", Sector.IT);
            userRepository.saveAll(Arrays.asList(user1, user2, user3, user4, user5, user6));
            userRepository.findAll().forEach(user -> System.out.println("User: " + user.getEmail()));

            projectRepository.deleteAll();
            Project project1 = new Project(LocalDate.of(2025, 1, 15), "Projeto de automacao", user1, "Automacao", Scope.ENG, Status.IN_PROGRESS);
            Project project2 = new Project(LocalDate.of(2024, 12, 23), "Projeto de vendas", user2, "Vendas", Scope.COM, Status.IN_PROGRESS);
            Project project3 = new Project(LocalDate.of(2024, 11, 20), "Projeto de marketing", user5, "Marketing", Scope.MKT, Status.PENDING);
            project1.setMembers(Arrays.asList(user4, user6));
            project2.setMembers(Arrays.asList(user3, user5));
            projectRepository.saveAll(Arrays.asList(project1, project2, project3));
            projectRepository.findAll().forEach(project -> System.out.println("Project: " + project.getName()));
        };
    }
}
