package solutions.skydev.pos.auth_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.auth_service.exception.AuthExceptions;
import solutions.skydev.pos.auth_service.model.entity.Account;
import solutions.skydev.pos.auth_service.model.entity.User;
import solutions.skydev.pos.auth_service.model.result.AuthResult;
import solutions.skydev.pos.auth_service.repository.AuthRepository;
import solutions.skydev.pos.auth_service.repository.UserRepository;
import solutions.skydev.pos.auth_service.util.JwtUtil;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthServiceImpl(AuthRepository authRepository, RefreshTokenService refreshTokenService,
                           JwtUtil jwtUtil, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResult authenticate(Account account) {
        Account existingAccount;

        if (account.getUsername() != null && account.getPassword() != null) {
            existingAccount = authRepository.findByUsername(account.getUsername())
                    .orElseThrow(() -> new AuthExceptions.UserNotFoundException("Account not found."));

            if (!passwordEncoder.matches(account.getPassword(), existingAccount.getPassword())) {
                throw new AuthExceptions.InvalidCredentialsException("Invalid username or password");
            }

        } else if (account.getUser() != null && account.getUser().getId() != null) {
            Long userId = account.getUser().getId();

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new AuthExceptions.UserNotFoundException("User not found."));

            existingAccount = user.getAccount();

            if (existingAccount == null) {
                throw new AuthExceptions.UserNotFoundException("Account not linked to user.");
            }

        } else {
            throw new AuthExceptions.InvalidCredentialsException("Insufficient login data provided.");
        }

        String subject = existingAccount.getUsername() != null
                ? existingAccount.getUsername()
                : existingAccount.getUser().getFullName();

        String role = Boolean.TRUE.equals(existingAccount.getAdmin())
                ? "ADMIN"
                : "EMPLOYEE";

        String accessToken = jwtUtil.generateToken(subject, role);
        String refreshToken = refreshTokenService.generateRefreshToken(existingAccount);

        return new AuthResult(accessToken, refreshToken);
    }


    @Override
    public Account register(Account account) {
        Optional<Account> existingUser = authRepository.findByUsername(account.getUsername());
        if (existingUser.isPresent()) {
            throw new AuthExceptions.UserAlreadyExistsException("Account with this username already exists");
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return authRepository.save(account);
    }

    @Override
    public void createUser(Account account, String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        Account adminAccount = authRepository.findByUsername(username)
                .orElseThrow(() -> new AuthExceptions.UserNotFoundException("Admin account not found"));
        if (!adminAccount.getAdmin()) {
            throw new AuthExceptions.InvalidCredentialsException("Only admin can create users");
        }
        if (account.getAdmin() != null && account.getAdmin()) {
            throw new AuthExceptions.InvalidCredentialsException("Cannot assign admin role to new account");
        }
        if (authRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new AuthExceptions.UserAlreadyExistsException("Account with this username already exists");
        }
        authRepository.save(account);
    }

    @Override
    public void deleteUser(String username, String token) {
        Account account = authRepository.findByUsername(username)
                .orElseThrow(() -> new AuthExceptions.UserNotFoundException("Account not found"));
        authRepository.delete(account);
    }

    @Override
    public void updateUser(Account account, String token) {
        Account existingAccount = authRepository.findByUsername(account.getUsername())
                .orElseThrow(() -> new AuthExceptions.UserNotFoundException("Account not found"));

        if (account.getPassword() != null && !account.getPassword().isEmpty()) {
            existingAccount.setPassword(passwordEncoder.encode(account.getPassword()));
        }
        if (account.getAdmin() != null) {
            existingAccount.setAdmin(account.getAdmin());
        }

        authRepository.save(existingAccount);
    }

    @Override
    public List<Account> getUsers(String token) {
        List<Account> accounts = authRepository.findAll();
        if (accounts.isEmpty()) {
            throw new AuthExceptions.UserNotFoundException("No accounts found in the system");
        }
        return accounts;
    }
}
