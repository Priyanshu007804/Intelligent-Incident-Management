package com.priyanshu.iims.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.priyanshu.iims.model.User;
import com.priyanshu.iims.model.enums.Role;
import com.priyanshu.iims.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepository.findByEmail("admin@iims.com") == null) {

                User admin = new User(
                        "admin@iims.com",
                        passwordEncoder.encode("Admin@123"),
                        Role.ADMIN
                );

                userRepository.save(admin);
            }

            if (userRepository.findByEmail("engineer@iims.com") == null) {

                User engineer = new User(
                        "engineer@iims.com",
                        passwordEncoder.encode("Engineer@123"),
                        Role.ENGINEER
                );

                userRepository.save(engineer);
            }
        };
    }
}