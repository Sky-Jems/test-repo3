package solutions.skydev.pos.discount_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import solutions.skydev.pos.common.discount_service.dto.request.DiscountOrderLineItemRequestDto;
import solutions.skydev.pos.discount_service.model.entity.Discount;
import solutions.skydev.pos.discount_service.model.entity.LineItemLevelDiscountOrder;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LineItemLevelDiscountOrderMapper {

    @Mapping(source = "lineItemId", target = "lineItemId")
    @Mapping(source = "discountId", target = "discount")
    LineItemLevelDiscountOrder toEntity(DiscountOrderLineItemRequestDto dto);

    default List<LineItemLevelDiscountOrder> toEntityList(List<DiscountOrderLineItemRequestDto> dtos, Long orderId) {
        if (dtos == null) return List.of();
        return dtos.stream().map(dto -> {
            LineItemLevelDiscountOrder entity = toEntity(dto);
            entity.setOrderId(orderId);
            return entity;
        }).toList();
    }

    default Discount map(Long discountId) {
        if (discountId == null) return null;
        Discount discount = new Discount();
        discount.setId(discountId);
        return discount;
    }
}

