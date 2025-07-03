package solutions.skydev.pos.auth_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.auth_service.model.entity.Account;
import solutions.skydev.pos.auth_service.model.entity.User;
import solutions.skydev.pos.auth_service.model.result.AuthResult;
import solutions.skydev.pos.auth_service.repository.UserRepository;
import solutions.skydev.pos.auth_service.util.JwtUtil;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final AuthServiceImpl authService;

    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil,
                           RefreshTokenService refreshTokenService, AuthServiceImpl authService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.authService = authService;
    }

    @Override
    public AuthResult authenticate(User user) {
        User existingUser = userRepository.existsByFirstNameAndLastName(user.getFirstName(), user.getLastName())
                ? userRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName())
                : null;

        String accessToken = jwtUtil.generateToken(existingUser != null ? existingUser.getFullName() : null,"EMPLOYEE");
        String refreshToken = refreshTokenService.generateRefreshToken(existingUser);

        return new AuthResult(accessToken, refreshToken);
    }

    @Override
    public User getUserByUsername(String username) {
        if (isBlank(username)) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        User user = userRepository.findByAccount_Username(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found with username: " + username);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new IllegalArgumentException("No users found");
        }
        return users;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    @Override
    public User createUser(User user) {
        if (user == null
                || isBlank(user.getFirstName())
                || isBlank(user.getLastName())) {
            throw new IllegalArgumentException("User or User data cannot be null or blank");
        }

        if (userExists(user.getFirstName(), user.getLastName())) {
            throw new IllegalArgumentException("User already exists with username: " + user.getFullName());
        }
        Account account = new Account();
        account.setUsername(
                user.getFirstName().toLowerCase().replaceAll("\\s+", "") +
                        "." +
                        user.getLastName().toLowerCase().replaceAll("\\s+", "")
        );
        account.setPassword("defaultPassword");
        account.setAdmin(false);
        account = authService.register(account);
        user.setAccount(account);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User or User ID cannot be null");
        }
        if (!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User with ID " + user.getId() + " does not exist");
        }
        return userRepository.save(user);
    }

    @Override
    public User deleteUser(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User or User ID cannot be null");
        }
        if (!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User with ID " + user.getId() + " does not exist");
        }
        userRepository.deleteById(user.getId());
        return user;
    }

    private boolean userExists(String firstName, String lastName) {
        return userRepository.existsByFirstNameAndLastName(firstName, lastName);
    }
}
