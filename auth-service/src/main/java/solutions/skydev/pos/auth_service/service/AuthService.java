package solutions.skydev.pos.auth_service.service;

import org.springframework.stereotype.Service;
import solutions.skydev.pos.auth_service.model.entity.User;
import solutions.skydev.pos.auth_service.model.result.AuthResult;

@Service
public interface AuthService {
    AuthResult authenticate(User user);
    void register(User user);
}
