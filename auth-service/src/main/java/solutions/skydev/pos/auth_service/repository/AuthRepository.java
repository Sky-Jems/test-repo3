package solutions.skydev.pos.auth_service.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.auth_service.model.entity.Account;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);
}
