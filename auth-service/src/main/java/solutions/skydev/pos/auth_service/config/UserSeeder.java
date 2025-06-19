package solutions.skydev.pos.auth_service.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import solutions.skydev.pos.auth_service.model.entity.User;
import solutions.skydev.pos.auth_service.service.AuthService;

@Configuration
public class UserSeeder {

    @Bean
    public CommandLineRunner seedUsers(AuthService authService) {
        return args -> {
            User user1 = new User();
            user1.setUsername("testuser");
            user1.setPassword("password123");
            try {
                authService.register(user1);
            } catch (Exception ignored) {}

            User user2 = new User();
            user2.setUsername("testadmin");
            user2.setPassword("admin123");
            try {
                authService.register(user2);
            } catch (Exception ignored) {}

            User user3 = new User();
            user3.setUsername("teststaff");
            user3.setPassword("staff123");
            try {
                authService.register(user3);
            } catch (Exception ignored) {}
        };
    }
}