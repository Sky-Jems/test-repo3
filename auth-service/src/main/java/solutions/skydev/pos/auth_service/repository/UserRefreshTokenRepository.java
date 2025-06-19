package solutions.skydev.pos.auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.auth_service.model.entity.User;
import solutions.skydev.pos.auth_service.model.entity.UserRefreshToken;

import java.util.Optional;
import java.util.List;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
    Optional<UserRefreshToken> findByRefreshToken(String refreshToken);
    List<UserRefreshToken> findAllByUserId(Long userId);
    void deleteByRefreshToken(String refreshToken);
    void deleteByUser(User user);
}
