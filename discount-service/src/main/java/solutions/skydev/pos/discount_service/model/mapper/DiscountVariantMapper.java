package solutions.skydev.pos.discount_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import solutions.skydev.pos.discount_service.model.dto.request.DiscountVariantRequestDto;
import solutions.skydev.pos.discount_service.model.dto.response.DiscountVariantResponseDto;
import solutions.skydev.pos.discount_service.model.entity.Discount;
import solutions.skydev.pos.discount_service.model.entity.DiscountVariant;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiscountVariantMapper {
    @Mapping(target = "discount", source = "discountId")
    DiscountVariant toEntity(DiscountVariantRequestDto dto);

    default Discount map(Long discountId) {
        if (discountId == null) return null;
        Discount discount = new Discount();
        discount.setId(discountId);
        return discount;
    }

    @Mapping(source = "discount.id", target = "discountId")
    DiscountVariantResponseDto toDto(DiscountVariant discountVariant);
    List<DiscountVariantResponseDto> toDto(List<DiscountVariant> discountVariantList);
}
