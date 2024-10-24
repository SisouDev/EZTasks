package com.spring.EZTasks.config;

import com.spring.EZTasks.model.entities.user.User;
import com.spring.EZTasks.model.repositories.user.UserRepository;
import com.spring.EZTasks.utils.enums.user.Sector;
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
            userRepository.deleteAll();

            User user1 = new User("gabriel@gmail.com", "Gabriel", "senha123", "Engenheiro", Sector.ENG);
            User user2 = new User("jorge@gmail.com", "Jorge Santos", "@senha321", "Analista", Sector.MKT);
            User user3 = new User("anelisa@ig.com", "Anelisa", "@senha22", "Analista", Sector.COM);
            User user4 = new User("joao@ig.com", "Joao Pedro", "password123", "Engenheiro", Sector.IT);
            User user5 = new User("tiago@gmail.com", "Tiago Paulo", "@password321", "Analista", Sector.MKT);
            User user6 = new User("afonso@ig.com", "Afonso Luis", "password123", "Engenheiro", Sector.IT);

            userRepository.saveAll(Arrays.asList(user1, user2, user3, user4, user5, user6));
        };
    }
}
