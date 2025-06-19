package solutions.skydev.pos.auth_service.model.mapper;

import org.mapstruct.Mapper;
import solutions.skydev.pos.auth_service.model.dto.request.RefreshTokenRequestDto;
import solutions.skydev.pos.auth_service.model.entity.UserRefreshToken;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {
    // Created a separate mapper to be used in the future
    // when refresh token becomes complex like (Admin needing session management, etc.)
    UserRefreshToken toEntity(RefreshTokenRequestDto refreshToken);
}
