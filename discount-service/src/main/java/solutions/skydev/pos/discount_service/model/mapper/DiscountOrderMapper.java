package solutions.skydev.pos.discount_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import solutions.skydev.pos.discount_service.model.dto.request.DiscountOrderRequestDto;
import solutions.skydev.pos.discount_service.model.dto.response.DiscountOrderResponseDto;
import solutions.skydev.pos.discount_service.model.entity.Discount;
import solutions.skydev.pos.discount_service.model.entity.DiscountOrder;

@Mapper(componentModel = "spring")
public interface DiscountOrderMapper {
    @Mapping(target = "discount", source = "discountId")
    DiscountOrder toEntity(DiscountOrderRequestDto discountOrderRequestDto);

    default Discount map(Long discountId) {
        if (discountId == null) return null;
        Discount discount = new Discount();
        discount.setId(discountId);
        return discount;
    }

    @Mapping(source = "discount", target = "discountId")
    DiscountOrderResponseDto toDto(DiscountOrder discountOrder);

    default Long map(Discount discount) {
        return discount != null ? discount.getId() : null;
    }
}
