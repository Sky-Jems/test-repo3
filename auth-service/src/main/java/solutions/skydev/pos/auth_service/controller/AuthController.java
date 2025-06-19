package solutions.skydev.pos.auth_service.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solutions.skydev.pos.auth_service.model.dto.request.AuthRequestDto;
import solutions.skydev.pos.auth_service.model.dto.request.RefreshTokenRequestDto;
import solutions.skydev.pos.auth_service.model.dto.response.AuthResponseDto;
import solutions.skydev.pos.auth_service.model.entity.User;
import solutions.skydev.pos.auth_service.model.mapper.AuthMapper;
import solutions.skydev.pos.auth_service.model.mapper.RefreshTokenMapper;
import solutions.skydev.pos.auth_service.model.result.AuthResult;
import solutions.skydev.pos.auth_service.service.AuthService;
import solutions.skydev.pos.auth_service.service.RefreshTokenService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthMapper authMapper;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenMapper refreshTokenMapper;

    @Autowired
    public AuthController(AuthService authService, AuthMapper authMapper,
                          RefreshTokenService refreshTokenService, RefreshTokenMapper refreshTokenMapper) {
        this.authService = authService;
        this.authMapper = authMapper;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenMapper = refreshTokenMapper;
    }

    @PostMapping("/login")
     public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto loginRequest) {
         User loginUser = authMapper.toEntity(loginRequest);
         AuthResult tokens = authService.authenticate(loginUser);
         AuthResponseDto response = authMapper.toDto(tokens);
         return ResponseEntity.ok(response);
     }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody AuthRequestDto registerRequest) {
        User registerUser = authMapper.toEntity(registerRequest);
        authService.register(registerUser);
        return ResponseEntity.ok().build();
     }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@Valid @RequestBody RefreshTokenRequestDto refreshRequest) {
        AuthResult refreshedTokens = refreshTokenService.refreshTokens(refreshTokenMapper.toEntity(refreshRequest));
        AuthResponseDto response = authMapper.toDto(refreshedTokens);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequestDto logoutRequest) {
        refreshTokenService.revokeRefreshToken(logoutRequest.getRefreshToken());
        return ResponseEntity.ok().build();
    }

}
