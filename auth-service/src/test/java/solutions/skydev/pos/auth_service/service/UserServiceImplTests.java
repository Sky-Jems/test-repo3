package solutions.skydev.pos.auth_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import solutions.skydev.pos.auth_service.model.entity.Account;
import solutions.skydev.pos.auth_service.model.entity.User;
import solutions.skydev.pos.auth_service.repository.UserRepository;
import solutions.skydev.pos.auth_service.util.JwtUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private AuthServiceImpl authService;

    @InjectMocks
    private UserServiceImpl userService;

    private User buildUser(Long id, String firstName, String lastName) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }

    private Account buildAccount(String username) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword("defaultPassword");
        return account;
    }

    @Test
    @DisplayName("getUserByUsername should return user if found")
    void getUserByUsername_found() {
        User user = buildUser(1L, "Jane", "Doe");
        Account account = buildAccount("jane.doe");
        user.setAccount(account);

        when(userRepository.findByAccount_Username("jane.doe")).thenReturn(user);

        User result = userService.getUserByUsername("jane.doe");

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
    }

    @Test
    @DisplayName("getUserByUsername should throw for null username")
    void getUserByUsername_null() {
        assertThrows(IllegalArgumentException.class, () ->
                userService.getUserByUsername(null));
    }

    @Test
    @DisplayName("getUserByUsername should throw for blank username")
    void getUserByUsername_blank() {
        assertThrows(IllegalArgumentException.class, () ->
                userService.getUserByUsername("   "));
    }

    @Test
    @DisplayName("getUserByUsername should throw if user not found")
    void getUserByUsername_notFound() {
        when(userRepository.findByAccount_Username("missing")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () ->
                userService.getUserByUsername("missing"));
    }

    @Test
    @DisplayName("getAllUsers should return users if present")
    void getAllUsers_success() {
        User user1 = buildUser(1L, "John", "Doe");
        User user2 = buildUser(2L, "Jane", "Smith");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("getAllUsers should throw if no users found")
    void getAllUsers_noUsers() {
        when(userRepository.findAll()).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class, () ->
                userService.getAllUsers());
    }

    @Test
    @DisplayName("createUser should save new user")
    void createUser_success() {
        User user = buildUser(null, "John", "Doe");
        Account account = buildAccount("john.doe");
        user.setAccount(account);

        when(userRepository.existsByFirstNameAndLastName("John", "Doe")).thenReturn(false);
        when(authService.register(any(Account.class))).thenReturn(account);
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(1L);
            return u;
        });

        User created = userService.createUser(user);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("john.doe", created.getAccount().getUsername());
    }

    @Test
    @DisplayName("createUser should throw if user data invalid")
    void createUser_invalidData() {
        User user = null;
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));

        User blankFirst = buildUser(null, "", "Doe");
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(blankFirst));

        User blankLast = buildUser(null, "John", "");
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(blankLast));
    }

    @Test
    @DisplayName("createUser should throw if user already exists")
    void createUser_userExists() {
        User user = buildUser(null, "John", "Doe");

        when(userRepository.existsByFirstNameAndLastName("John", "Doe")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }

    @Test
    @DisplayName("updateUser should save user if exists")
    void updateUser_success() {
        User user = buildUser(1L, "John", "Doe");

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.save(user)).thenReturn(user);

        User updated = userService.updateUser(user);

        assertNotNull(updated);
        assertEquals(1L, updated.getId());
    }

    @Test
    @DisplayName("updateUser should throw if user is null")
    void updateUser_null() {
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(null));
    }

    @Test
    @DisplayName("updateUser should throw if user does not exist")
    void updateUser_notExist() {
        User user = buildUser(2L, "Jane", "Smith");

        when(userRepository.existsById(2L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(user));
    }

    @Test
    @DisplayName("deleteUser should delete existing user")
    void deleteUser_success() {
        User user = buildUser(3L, "Bob", "Marley");

        when(userRepository.existsById(3L)).thenReturn(true);

        User deleted = userService.deleteUser(user);

        verify(userRepository).deleteById(3L);
        assertEquals(user, deleted);
    }

    @Test
    @DisplayName("deleteUser should throw if user is null")
    void deleteUser_null() {
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(null));
    }

    @Test
    @DisplayName("deleteUser should throw if user does not exist")
    void deleteUser_notExist() {
        User user = buildUser(5L, "Carl", "Johnson");

        when(userRepository.existsById(5L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(user));
    }
}
