package solutions.skydev.pos.order_orchestrator_service.model.mapper;

import org.mapstruct.Mapper;
import solutions.skydev.pos.order_orchestrator_service.model.dto.request.CartRequestDto;
import solutions.skydev.pos.order_orchestrator_service.model.entity.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartRequestDto toDto(Cart cart);
    Cart toEntity(CartRequestDto cartRequestDto);
}
