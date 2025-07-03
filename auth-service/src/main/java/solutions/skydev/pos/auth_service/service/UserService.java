package solutions.skydev.pos.auth_service.service;

import solutions.skydev.pos.auth_service.model.entity.User;
import solutions.skydev.pos.auth_service.model.result.AuthResult;

import java.util.List;

public interface UserService {
    AuthResult authenticate(User user);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    User createUser(User user);

    User updateUser(User user);

    User deleteUser(User user);
}
