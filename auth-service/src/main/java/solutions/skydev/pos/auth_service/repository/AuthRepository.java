package solutions.skydev.pos.auth_service.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.auth_service.model.entity.User;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
