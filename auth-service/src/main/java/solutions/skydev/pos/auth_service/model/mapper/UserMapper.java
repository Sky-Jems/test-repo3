package solutions.skydev.pos.auth_service.model.mapper;

import org.mapstruct.Mapper;
import solutions.skydev.pos.common.auth_service.dto.request.UserRequestDto;
import solutions.skydev.pos.common.auth_service.dto.response.UserResponseDto;
import solutions.skydev.pos.auth_service.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Define mapping methods here if needed
    // For example, you might have methods to convert between User entities and DTOs
    // UserDto toDto(User user);
     User toEntity(UserRequestDto userDto);
     UserResponseDto toDto(User user);
}
