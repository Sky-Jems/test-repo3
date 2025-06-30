package solutions.skydev.pos.discount_service.model.mapper;

import org.mapstruct.Mapper;
import solutions.skydev.pos.common.discount_service.dto.response.DiscountLineItemResponseDto;
import solutions.skydev.pos.discount_service.model.entity.DiscountLineItem;

@Mapper(componentModel = "spring")
public interface DiscountLineItemMapper {
    // Todo: when multiple discounts are supported, refactor this to be able to accept and return List of DiscountLineItem
    DiscountLineItemResponseDto toDto(DiscountLineItem discountLineItem);
}
