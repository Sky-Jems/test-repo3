package solutions.skydev.pos.auth_service.model.mapper;

import org.mapstruct.Mapper;
import solutions.skydev.pos.auth_service.model.dto.request.AuthRequestDto;
import solutions.skydev.pos.auth_service.model.dto.response.AuthResponseDto;
import solutions.skydev.pos.auth_service.model.entity.User;
import solutions.skydev.pos.auth_service.model.result.AuthResult;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    User toEntity(AuthRequestDto authRequestDto);
    AuthResponseDto toDto(AuthResult authResult);
}
