package solutions.skydev.pos.auth_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.skydev.pos.common.auth_service.dto.request.UserRequestDto;
import solutions.skydev.pos.common.auth_service.dto.response.UserResponseDto;
import solutions.skydev.pos.auth_service.model.entity.User;
import solutions.skydev.pos.auth_service.model.mapper.UserMapper;
import solutions.skydev.pos.auth_service.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/get-user/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDto> userDtos = users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @PostMapping("/create-user")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) {
        User user = userMapper.toEntity(userRequestDto);
        user = userService.createUser(user);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PutMapping("/update-user")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UserRequestDto userRequestDto) {
        User user = userMapper.toEntity(userRequestDto);
        user = userService.updateUser(user);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<UserResponseDto> deleteUser(@RequestBody UserRequestDto userRequestDto) {
        User user = userMapper.toEntity(userRequestDto);
        user = userService.deleteUser(user);
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
