package solutions.skydev.pos.auth_service.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.skydev.pos.common.auth_service.dto.request.AuthRequestDto;
import solutions.skydev.pos.common.auth_service.dto.request.RefreshTokenRequestDto;
import solutions.skydev.pos.common.auth_service.dto.response.AuthResponseDto;
import solutions.skydev.pos.auth_service.model.entity.Account;
import solutions.skydev.pos.auth_service.model.mapper.AuthMapper;
import solutions.skydev.pos.auth_service.model.mapper.RefreshTokenMapper;
import solutions.skydev.pos.auth_service.model.result.AuthResult;
import solutions.skydev.pos.auth_service.service.AuthService;
import solutions.skydev.pos.auth_service.service.RefreshTokenService;

import java.util.List;

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
         Account loginAccount = authMapper.toEntity(loginRequest);
         AuthResult tokens = authService.authenticate(loginAccount);
         AuthResponseDto response = authMapper.toDto(tokens);
         return ResponseEntity.ok(response);
     }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody AuthRequestDto registerRequest) {
        Account registerAccount = authMapper.toEntity(registerRequest);
        authService.register(registerAccount);
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
