package solutions.skydev.pos.discount_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import solutions.skydev.pos.discount_service.model.dto.request.ApplyDiscountRequestDto;
import solutions.skydev.pos.discount_service.model.dto.request.DiscountRequestDto;
import solutions.skydev.pos.discount_service.model.dto.request.LineItemDto;
import solutions.skydev.pos.discount_service.model.dto.response.ApplyDiscountResponseDto;
import solutions.skydev.pos.discount_service.model.dto.response.DiscountResponseDto;
import solutions.skydev.pos.discount_service.model.entity.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DiscountMapper {
    @Mapping(source = "discountId", target = "id")
    Discount toEntity(ApplyDiscountRequestDto discountRequestDto);
    @Mapping(source = "variants", target = "variants")
    Discount toEntity(DiscountRequestDto dto);

    default LocalDateTime map(String value) {
        return value != null ? LocalDateTime.parse(value) : null;
    }

    @Mapping(source = "orderId", target = "id")
    Order toOrderEntity(ApplyDiscountRequestDto discountRequestDto);

    LineItem toLineItem(LineItemDto lineItemDto);

    List<DiscountResponseDto> toDto(List<Discount> discounts);

    DiscountResponseDto toDto(Discount discounts);
    default String map(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toString() : null;
    }
    @Mapping(source = "discount.id", target = "discountId")
    ApplyDiscountResponseDto toDto(DiscountLineItem discountLineItem);
}
