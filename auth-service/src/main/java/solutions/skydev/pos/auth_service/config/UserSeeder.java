package solutions.skydev.pos.auth_service.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import solutions.skydev.pos.auth_service.model.entity.Account;
import solutions.skydev.pos.auth_service.model.entity.User;
import solutions.skydev.pos.auth_service.service.AuthService;
import solutions.skydev.pos.auth_service.service.UserService;

@Configuration
public class UserSeeder {

    @Bean
    public CommandLineRunner seedUsers(AuthService authService, UserService userService) {
        return args -> {
            Account account1 = new Account();
            account1.setUsername("admin");
            account1.setPassword("password123");
            account1.setAdmin(true);
            try {
                authService.register(account1);
            } catch (Exception ignored) {}

            User user1 = new User();
            user1.setFirstName("Employee");
            user1.setLastName("1");
            try {
                userService.createUser(user1);
            } catch (Exception ignored) {}

            User user2 = new User();
            user2.setFirstName("Employee");
            user2.setLastName("2");
            try {
                userService.createUser(user2);
            } catch (Exception ignored) {}

            User user3 = new User();
            user3.setFirstName("Employee");
            user3.setLastName("3");
            try {
                userService.createUser(user3);
            } catch (Exception ignored) {}
        };
    }
}
