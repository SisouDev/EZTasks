package com.spring.EZTasks.config;

import com.spring.EZTasks.model.entities.user.User;
import com.spring.EZTasks.model.repositories.user.UserRepository;
import com.spring.EZTasks.utils.enums.Sector;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@Profile("test")
public class DataSeed {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            User user1 = new User(
                    "fulano@gmail.com", "Fulano",
                    "senha123", "Engenheiro", Sector.ENG
            );

            User user2 = new User(
                    "email@gmail.com", "Ciclano",
                    "@senha123", "Analista", Sector.IT
            );

            User user3 = new User(
                    "emailteste@ig.com", "Pedro",
                    "@senha123", "Analista", Sector.IT
            );

            userRepository.saveAll(Arrays.asList(user1, user2, user3));
        };
    }
}
