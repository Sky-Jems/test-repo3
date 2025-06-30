package solutions.skydev.pos.discount_service.model.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import solutions.skydev.pos.common.discount_service.dto.request.DiscountOrderLineItemRequestDto;
import solutions.skydev.pos.common.discount_service.dto.request.DiscountOrderRequestDto;
import solutions.skydev.pos.common.discount_service.dto.response.DiscountOrderUpdatedResponseDto;
import solutions.skydev.pos.common.discount_service.dto.response.LineItemLevelDiscountOrderResponseDto;
import solutions.skydev.pos.discount_service.model.entity.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DiscountOrderMapper {

    @Mapping(target = "discount", source = "discountId")
    LineItemLevelDiscountOrder toLineItemLevelEntity(DiscountOrderLineItemRequestDto dto, @Context Long orderId);
    List<LineItemLevelDiscountOrder> toLineItemLevelEntities(List<DiscountOrderLineItemRequestDto> dtos, @Context Long orderId);

    @Mapping(target = "discount", source = "discountId")
    OrderLevelDiscountOrder toOrderLevelEntity(DiscountOrderRequestDto dto);

    default Discount map(Long id) {
        if (id == null) return null;
        Discount d = new Discount();
        d.setId(id);
        return d;
    }

    @Mapping(source = "orderId", target = "id")
    Order toOrderEntity(DiscountOrderRequestDto discountOrderRequestDto);

    LineItem toLineItem(DiscountOrderLineItemRequestDto lineItemDto);

    @Mapping(target = "orderId", source = "orderId")
    @Mapping(target = "lineItems", expression = "java(toLineItemLevelDiscountOrderResponseDtoList(discountOrders))")
    @Mapping(target = "discountAmount", expression = "java(extractOrderLevelDiscountAmount(discountOrders))")
    @Mapping(target = "discountId", expression = "java(extractOrderLevelDiscountId(discountOrders))")
    DiscountOrderUpdatedResponseDto toDto(List<DiscountOrder> discountOrders, Long orderId);

    @Mapping(target = "discountId", source = "discount.id")
    LineItemLevelDiscountOrderResponseDto toLineItemLevelDiscountOrderResponseDto(LineItemLevelDiscountOrder discountOrder);

    default List<LineItemLevelDiscountOrderResponseDto> toLineItemLevelDiscountOrderResponseDtoList(List<DiscountOrder> discountOrders) {
        return discountOrders.stream()
                .filter(d -> d instanceof LineItemLevelDiscountOrder)
                .map(d -> toLineItemLevelDiscountOrderResponseDto((LineItemLevelDiscountOrder) d))
                .collect(Collectors.toList());
    }

    default BigDecimal extractOrderLevelDiscountAmount(List<DiscountOrder> discountOrders) {
        return discountOrders.stream()
                .filter(d -> d instanceof OrderLevelDiscountOrder)
                .map(DiscountOrder::getDiscountAmount)
                .findFirst()
                .orElse(null);
    }

    default Long extractOrderLevelDiscountId(List<DiscountOrder> discountOrders) {
        return discountOrders.stream()
                .filter(d -> d instanceof OrderLevelDiscountOrder)
                .map(d -> ((OrderLevelDiscountOrder) d).getDiscount().getId())
                .findFirst()
                .orElse(null);
    }
}
