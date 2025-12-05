package com.springsecurity.springsecurity.configuration;

import com.springsecurity.springsecurity.entity.user.User;
import com.springsecurity.springsecurity.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AdminConfig {

        @Bean
        CommandLineRunner createAdmin(UserRepository userRepository) {
            return args -> {
                String adminEmail = "a@gmail.com";
                if(userRepository.findByEmail(adminEmail).isEmpty()) {
                    User admin = new User();
                    admin.setEmail(adminEmail);
                    admin.setPassword(new BCryptPasswordEncoder().encode("111")); // default password
                    admin.setRole("ADMIN");
                    admin.setVerified(true);
                    userRepository.save(admin);
                    System.out.println("✅ Admin created successfully!");
                } else {
                    System.out.println("Admin already exists, skipping creation.");
                }
            };
        }
    }
