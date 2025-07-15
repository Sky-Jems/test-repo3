package solutions.skydev.pos.discount_service.model.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import solutions.skydev.pos.common.discount_service.dto.request.DiscountOrderLineItemRequestDto;
import solutions.skydev.pos.common.discount_service.dto.request.DiscountOrderRequestDto;
import solutions.skydev.pos.common.discount_service.dto.response.DiscountOrderUpdatedResponseDto;
import solutions.skydev.pos.common.discount_service.dto.response.DiscountResponseDto;
import solutions.skydev.pos.common.discount_service.dto.response.LineItemLevelDiscountOrderResponseDto;
import solutions.skydev.pos.discount_service.model.DiscountOrderSummary;
import solutions.skydev.pos.discount_service.model.entity.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
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
    @Mapping(target = "discountAmount", source = "discountAmount")
    @Mapping(target = "discount", expression = "java(toOrderLevelDiscount(summary.getDiscountOrders()))")
    @Mapping(target = "lineItems", expression = "java(toLineItemLevelDiscountOrderResponseDtoList(summary.getDiscountOrders()))")
    DiscountOrderUpdatedResponseDto toDto(DiscountOrderSummary summary);


    default DiscountResponseDto toOrderLevelDiscount(List<DiscountOrder> discountOrders) {
        if (discountOrders == null) return null;

        return discountOrders.stream()
                .filter(d -> d instanceof OrderLevelDiscountOrder)
                .map(OrderLevelDiscountOrder.class::cast)
                .map(OrderLevelDiscountOrder::getDiscount)
                .filter(Objects::nonNull)
                .map(this::toDiscountResponseDto)
                .findFirst()
                .orElse(null);
    }

    default LineItemLevelDiscountOrderResponseDto toLineItemLevelDiscountOrderResponseDto(LineItemLevelDiscountOrder discountOrder) {
        if (discountOrder == null) {
            return null;
        }

        return LineItemLevelDiscountOrderResponseDto.builder()
                .id(discountOrder.getId())
                .lineItemId(discountOrder.getLineItemId())
                .discountAmount(discountOrder.getDiscountAmount())
                .discount(toDiscountResponseDto(discountOrder.getDiscount()))
                .build();
    }

    default List<LineItemLevelDiscountOrderResponseDto> toLineItemLevelDiscountOrderResponseDtoList(List<DiscountOrder> discountOrders) {
        return discountOrders.stream()
                .filter(d -> d instanceof LineItemLevelDiscountOrder)
                .map(d -> toLineItemLevelDiscountOrderResponseDto((LineItemLevelDiscountOrder) d))
                .collect(Collectors.toList());
    }

    default DiscountResponseDto toDiscountResponseDto(Discount discount) {
        if (discount == null) return null;

        return DiscountResponseDto.builder()
                .id(discount.getId())
                .name(discount.getName())
                .type(discount.getType() != null ? discount.getType().name() : null)
                .build();
    }


}
