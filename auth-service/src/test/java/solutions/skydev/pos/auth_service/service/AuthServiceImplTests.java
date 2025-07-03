package solutions.skydev.pos.auth_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import solutions.skydev.pos.auth_service.exception.AuthExceptions;
import solutions.skydev.pos.common.auth_service.dto.response.AuthResponseDto;
import solutions.skydev.pos.auth_service.model.entity.Account;
import solutions.skydev.pos.auth_service.model.mapper.AuthMapper;
import solutions.skydev.pos.auth_service.model.result.AuthResult;
import solutions.skydev.pos.auth_service.repository.AuthRepository;
import solutions.skydev.pos.auth_service.util.JwtUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTests {

    @Mock
    AuthRepository authRepository;

    @Mock
    JwtUtil jwtUtil;

    @Mock
    RefreshTokenService refreshTokenService;

    @Mock
    AuthMapper authMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private PasswordEncoder encoder;

    private Account buildUser(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setAdmin(true);
        account.setPassword(password);
        return account;
    }

    @BeforeEach
    void setUp() {
        encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Test
    @DisplayName("Should register a new user and call repository save")
    void registerUser_success() {
        Account account = buildUser("testuser", "password");

        when(authRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        authService.register(account);

        verify(authRepository).save(any(Account.class));
    }

    @Test
    @DisplayName("Should throw when registering an already existing user")
    void registerUser_alreadyExists() {
        Account account = buildUser("testuser", "password");
        when(authRepository.findByUsername("testuser")).thenReturn(Optional.of(account));

        assertThrows(AuthExceptions.UserAlreadyExistsException.class, () -> authService.register(account));
    }

    @Test
    @DisplayName("Should authenticate valid user and return tokens")
    void authenticateUser_success() {
        String username = "testuser";
        String rawPassword = "password";
        String encodedPassword = encoder.encode(rawPassword);
        String accessToken = "access-token";
        String refreshToken = "refresh-token";

        Account dbAccount = buildUser(username, encodedPassword);
        Account loginAccount = buildUser(username, rawPassword);

        when(authRepository.findByUsername(username)).thenReturn(Optional.of(dbAccount));
        when(jwtUtil.generateToken(username, "ADMIN")).thenReturn(accessToken);
        when(refreshTokenService.generateRefreshToken(dbAccount)).thenReturn(refreshToken);

        AuthResult tokens = authService.authenticate(loginAccount);

        assertNotNull(tokens);
        assertEquals(accessToken, tokens.accessToken());
        assertEquals(refreshToken, tokens.refreshToken());

        AuthResponseDto mockResponse = new AuthResponseDto(accessToken, refreshToken);
        when(authMapper.toDto(tokens)).thenReturn(mockResponse);

        AuthResponseDto response = authMapper.toDto(tokens);
        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
    }

    @Test
    @DisplayName("Should throw when authenticating non-existing user")
    void authenticateUser_userNotFound() {
        when(authRepository.findByUsername("missing")).thenReturn(Optional.empty());

        Account account = buildUser("missing", "any");

        assertThrows(AuthExceptions.UserNotFoundException.class, () -> authService.authenticate(account));
    }

    @Test
    @DisplayName("Should throw when password does not match")
    void authenticateUser_invalidPassword() {
        String username = "testuser";
        Account dbAccount = buildUser(username, encoder.encode("correct"));
        Account loginAccount = buildUser(username, "wrong");

        when(authRepository.findByUsername(username)).thenReturn(Optional.of(dbAccount));

        assertThrows(AuthExceptions.InvalidCredentialsException.class, () -> authService.authenticate(loginAccount));
    }
}
