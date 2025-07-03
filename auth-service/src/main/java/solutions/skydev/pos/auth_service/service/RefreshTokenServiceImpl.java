package solutions.skydev.pos.auth_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solutions.skydev.pos.auth_service.exception.AuthExceptions;
import solutions.skydev.pos.auth_service.model.entity.Account;
import solutions.skydev.pos.auth_service.model.entity.User;
import solutions.skydev.pos.auth_service.model.entity.UserRefreshToken;
import solutions.skydev.pos.auth_service.model.result.AuthResult;
import solutions.skydev.pos.auth_service.repository.UserRefreshTokenRepository;
import solutions.skydev.pos.auth_service.util.JwtUtil;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final UserRefreshTokenRepository userRefreshTokenRepository;

    private static final int REFRESH_TOKEN_EXPIRY_DAYS = 7;
    private static final boolean SINGLE_REFRESH_TOKEN_PER_USER = true;
    private final JwtUtil jwtUtil;

    @Autowired
    public RefreshTokenServiceImpl(UserRefreshTokenRepository userRefreshTokenRepository,
                                   JwtUtil jwtUtil) {
        this.userRefreshTokenRepository = userRefreshTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String generateRefreshToken(Account account) {
        String refreshToken = createRefreshTokenString();

        if (SINGLE_REFRESH_TOKEN_PER_USER) {
            userRefreshTokenRepository.deleteByAccount(account);
        }

        UserRefreshToken tokenEntity = createRefreshTokenEntity(refreshToken, account);
        userRefreshTokenRepository.save(tokenEntity);

        return refreshToken;
    }

    @Override
    public String generateRefreshToken(User user) {
        return generateRefreshToken(user.getAccount());
    }

    @Override
    public AuthResult refreshTokens(UserRefreshToken refreshToken) {
        UserRefreshToken tokenEntity = userRefreshTokenRepository.findByRefreshToken(refreshToken.getRefreshToken())
                .orElseThrow(() -> new AuthExceptions.InvalidRefreshTokenException("Invalid refresh token"));

        validateRefreshToken(tokenEntity);

        Account account = tokenEntity.getAccount();
        String newAccessToken = jwtUtil.generateToken(account.getUsername(), account.getAdmin() ? "ADMIN" : "EMPLOYEE");
        String newRefreshToken = createRefreshTokenString();

        tokenEntity.setRefreshToken(newRefreshToken);
        tokenEntity.setExpiresAt(LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRY_DAYS));
        userRefreshTokenRepository.save(tokenEntity);

        return new AuthResult(newAccessToken, newRefreshToken);
    }

    private String createRefreshTokenString() {
        return UUID.randomUUID().toString();
    }

    private UserRefreshToken createRefreshTokenEntity(String refreshToken, Account account) {
        UserRefreshToken tokenEntity = new UserRefreshToken();
        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setAccount(account);
        tokenEntity.setExpiresAt(LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRY_DAYS));
        return tokenEntity;
    }

    @Override
    public void revokeRefreshToken(String refreshToken) {
        UserRefreshToken tokenEntity = userRefreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthExceptions.InvalidRefreshTokenException("Invalid refresh token"));

        userRefreshTokenRepository.delete(tokenEntity);
    }

    private void validateRefreshToken(UserRefreshToken tokenEntity) {
        if (tokenEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            userRefreshTokenRepository.delete(tokenEntity);
            throw new AuthExceptions.RefreshTokenExpiredException("Refresh token has expired");
        }
    }
}
