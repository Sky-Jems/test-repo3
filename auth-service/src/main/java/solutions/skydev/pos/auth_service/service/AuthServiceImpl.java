package solutions.skydev.pos.auth_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.auth_service.exception.AuthExceptions;
import solutions.skydev.pos.auth_service.model.entity.User;
import solutions.skydev.pos.auth_service.model.result.AuthResult;
import solutions.skydev.pos.auth_service.repository.AuthRepository;
import solutions.skydev.pos.auth_service.util.JwtUtil;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthServiceImpl(AuthRepository authRepository, RefreshTokenService refreshTokenService,
                           JwtUtil jwtUtil) {
        this.authRepository = authRepository;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResult authenticate(User user) {
        User existingUser = authRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new AuthExceptions.UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new AuthExceptions.InvalidCredentialsException("Invalid username or password");
        }

        String accessToken = jwtUtil.generateToken(existingUser.getUsername());
        String refreshToken = refreshTokenService.generateRefreshToken(existingUser);

        return new AuthResult(accessToken, refreshToken);
    }

    @Override
    public void register(User user) {
        Optional<User> existingUser = authRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new AuthExceptions.UserAlreadyExistsException("User with this username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        authRepository.save(user);
    }
}
