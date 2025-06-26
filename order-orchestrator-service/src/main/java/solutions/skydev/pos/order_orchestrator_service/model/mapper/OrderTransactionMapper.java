package solutions.skydev.pos.order_orchestrator_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import solutions.skydev.pos.common.order_orchestrator_service.dto.response.OrderTransactionResponseDto;
import solutions.skydev.pos.order_orchestrator_service.model.entity.OrderTransaction;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface OrderTransactionMapper {
    
//    OrderTransaction toEntity(OrderTransactionRequestDto orderTransactionRequestDto);
    OrderTransactionResponseDto toResponseDto(OrderTransaction orderTransaction);
}
