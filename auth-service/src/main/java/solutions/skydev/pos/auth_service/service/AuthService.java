package solutions.skydev.pos.auth_service.service;

import org.springframework.stereotype.Service;
import solutions.skydev.pos.auth_service.model.entity.Account;
import solutions.skydev.pos.auth_service.model.result.AuthResult;

import java.util.List;

@Service
public interface AuthService {
    AuthResult authenticate(Account account);
    Account register(Account account);
    void createUser(Account account, String token);
    void updateUser(Account account, String token);
    void deleteUser(String Username, String token);
    List<Account> getUsers(String token);

}
