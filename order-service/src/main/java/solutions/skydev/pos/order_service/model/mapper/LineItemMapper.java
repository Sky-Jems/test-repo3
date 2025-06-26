package solutions.skydev.pos.order_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import solutions.skydev.pos.common.order_service.dto.request.LineItemRequestDto;
import solutions.skydev.pos.common.order_service.dto.response.LineItemResponseDto;
import solutions.skydev.pos.order_service.model.entity.LineItem;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LineItemMapper {
    LineItemRequestDto toRequestDto(LineItem lineItem);

    @Mapping(target = "subTotal", ignore = true)
    @Mapping(source = "orderId", target = "order.id")
    @Mapping(target = "id", ignore = true)
    LineItem toEntityFromAdd(LineItemRequestDto lineItemRequestDto);
    
    @Mapping(source = "order.id", target = "orderId")
    LineItemResponseDto toResponseDto(LineItem lineItem);
    
    @Mapping(source = "orderId", target = "order.id")
    @Mapping(target = "subTotal", ignore = true)
    LineItem toEntityFromUpdate(LineItemRequestDto lineItemRequestDto);
    
    List<LineItemResponseDto> toResponseDtoList(List<LineItem> lineItems);
}
