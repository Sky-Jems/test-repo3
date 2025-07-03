package solutions.skydev.pos.auth_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import solutions.skydev.pos.auth_service.exception.AuthExceptions;
import solutions.skydev.pos.common.auth_service.dto.response.AuthResponseDto;
import solutions.skydev.pos.auth_service.model.entity.Account;
import solutions.skydev.pos.auth_service.model.entity.UserRefreshToken;
import solutions.skydev.pos.auth_service.model.mapper.AuthMapper;
import solutions.skydev.pos.auth_service.model.result.AuthResult;
import solutions.skydev.pos.auth_service.repository.UserRefreshTokenRepository;
import solutions.skydev.pos.auth_service.util.JwtUtil;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceImplTests {

    @Mock
    private UserRefreshTokenRepository tokenRepository;

    @Mock
    private AuthMapper authMapper;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    private Account account;
    private String sampleRefreshToken;

    @BeforeEach
    void setup() {
        account = new Account();
        account.setId(1L);
        account.setUsername("testuser");
        account.setAdmin(true);
        sampleRefreshToken = UUID.randomUUID().toString();
    }

    @Test
    @DisplayName("Should generate a new refresh token and save it")
    void GenerateRefreshToken() {
        doNothing().when(tokenRepository).deleteByAccount(account);
        when(tokenRepository.save(any(UserRefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String token = refreshTokenService.generateRefreshToken(account);

        assertNotNull(token);
        verify(tokenRepository).deleteByAccount(account);
        verify(tokenRepository).save(any(UserRefreshToken.class));
    }

    @Test
    @DisplayName("Should refresh tokens if the refresh token is valid")
    void RefreshTokens_Success() {
        UserRefreshToken existingToken = new UserRefreshToken();
        existingToken.setRefreshToken(sampleRefreshToken);
        existingToken.setAccount(account);
        existingToken.setExpiresAt(LocalDateTime.now().plusDays(1));

        String newAccessToken = "newAccessToken";
        String newRefreshToken = "newRefreshToken";

        when(tokenRepository.findByRefreshToken(sampleRefreshToken)).thenReturn(Optional.of(existingToken));
        when(jwtUtil.generateToken(account.getUsername(), "ADMIN")).thenReturn(newAccessToken);
        when(tokenRepository.save(any(UserRefreshToken.class))).thenAnswer(invocation -> {
            UserRefreshToken savedToken = invocation.getArgument(0);
            savedToken.setRefreshToken(newRefreshToken);
            return savedToken;
        });

        AuthResult tokens = refreshTokenService.refreshTokens(existingToken);

        assertNotNull(tokens);
        assertEquals(newAccessToken, tokens.accessToken());
        assertNotNull(tokens.refreshToken());

        AuthResponseDto mockResponse = new AuthResponseDto(newAccessToken, newRefreshToken);
        when(authMapper.toDto(tokens)).thenReturn(mockResponse);

        AuthResponseDto response = authMapper.toDto(tokens);
        assertNotNull(response);
        assertEquals(newAccessToken, response.getAccessToken());
        assertEquals(newRefreshToken, response.getRefreshToken());

        verify(tokenRepository).save(existingToken);
    }

    @Test
    @DisplayName("Should throw AuthExceptions.InvalidRefreshTokenException if refresh token is invalid")
    void RefreshTokens_InvalidToken() {
        UserRefreshToken inputToken = new UserRefreshToken();
        inputToken.setRefreshToken(sampleRefreshToken);

        when(tokenRepository.findByRefreshToken(sampleRefreshToken)).thenReturn(Optional.empty());

        assertThrows(AuthExceptions.InvalidRefreshTokenException.class,
                () -> refreshTokenService.refreshTokens(inputToken));
    }


    @Test
    @DisplayName("Should throw and delete if refresh token is expired")
    void RefreshTokens_ExpiredToken() {
        UserRefreshToken expiredToken = new UserRefreshToken();
        expiredToken.setRefreshToken(sampleRefreshToken);
        expiredToken.setAccount(account);
        expiredToken.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        when(tokenRepository.findByRefreshToken(sampleRefreshToken)).thenReturn(Optional.of(expiredToken));

        AuthExceptions.RefreshTokenExpiredException exception = assertThrows(
                AuthExceptions.RefreshTokenExpiredException.class,
                () -> refreshTokenService.refreshTokens(expiredToken)
        );

        assertEquals("Refresh token has expired", exception.getMessage());
        verify(tokenRepository).delete(expiredToken);
    }

    @Test
    @DisplayName("Should revoke existing refresh token")
    void RevokeRefreshToken() {
        UserRefreshToken existingToken = new UserRefreshToken();
        existingToken.setRefreshToken(sampleRefreshToken);

        when(tokenRepository.findByRefreshToken(sampleRefreshToken)).thenReturn(Optional.of(existingToken));

        refreshTokenService.revokeRefreshToken(sampleRefreshToken);

        verify(tokenRepository).delete(existingToken);
    }

    @Test
    @DisplayName("Should throw InvalidRefreshTokenException when revoking non-existent refresh token")
    void RevokeRefreshToken_NotFound() {
        when(tokenRepository.findByRefreshToken(sampleRefreshToken)).thenReturn(Optional.empty());

        assertThrows(AuthExceptions.InvalidRefreshTokenException.class,
                () -> refreshTokenService.revokeRefreshToken(sampleRefreshToken));
    }

}
