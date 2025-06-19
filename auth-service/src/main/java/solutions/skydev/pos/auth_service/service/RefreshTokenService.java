package solutions.skydev.pos.auth_service.service;

import solutions.skydev.pos.auth_service.model.entity.User;
import solutions.skydev.pos.auth_service.model.entity.UserRefreshToken;
import solutions.skydev.pos.auth_service.model.result.AuthResult;

// Created a separated service for refresh token operations
// to be ready in the future if it becomes complex like multi-session management, etc.
public interface RefreshTokenService {
    String generateRefreshToken(User user);
    AuthResult refreshTokens(UserRefreshToken refreshToken);
    void revokeRefreshToken(String refreshToken);
}
