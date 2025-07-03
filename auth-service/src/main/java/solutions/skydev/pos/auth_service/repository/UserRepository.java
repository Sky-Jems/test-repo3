package solutions.skydev.pos.auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.auth_service.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    User findByFirstNameAndLastName(String firstName, String lastName);

    User findByAccount_Username(String accountUsername);
}
