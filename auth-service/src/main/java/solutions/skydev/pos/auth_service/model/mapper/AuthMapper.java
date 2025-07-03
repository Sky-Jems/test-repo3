package solutions.skydev.pos.auth_service.model.mapper;

import jakarta.validation.groups.Default;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import solutions.skydev.pos.common.auth_service.dto.request.AuthRequestDto;
import solutions.skydev.pos.common.auth_service.dto.response.AuthResponseDto;
import solutions.skydev.pos.auth_service.model.entity.Account;
import solutions.skydev.pos.auth_service.model.entity.User;
import solutions.skydev.pos.auth_service.model.result.AuthResult;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    @Mapping(target = "user", source = "userId")
    Account toEntity(AuthRequestDto authRequestDto);

    default User map(Long id) {
        if (id == null) return null;
        User d = new User();
        d.setId(id);
        return d;
    }

    AuthResponseDto toDto(AuthResult authResult);
}
