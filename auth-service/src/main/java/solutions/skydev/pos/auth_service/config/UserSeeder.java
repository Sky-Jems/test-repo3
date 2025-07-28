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
            account1.setPassword("g7Mpl3#n8");
            account1.setAdmin(true);
            try {
                authService.register(account1);
            } catch (Exception ignored) {}

            User user1 = new User();
            user1.setFirstName("Renna");
            user1.setLastName("Magnifique");
            try {
                userService.createUser(user1);
            } catch (Exception ignored) {}

            User user2 = new User();
            user2.setFirstName("Mau");
            user2.setLastName("Magnifique");
            try {
                userService.createUser(user2);
            } catch (Exception ignored) {}
        };
    }
}
