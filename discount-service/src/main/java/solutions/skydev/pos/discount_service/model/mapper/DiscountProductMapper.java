package solutions.skydev.pos.discount_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import solutions.skydev.pos.common.discount_service.dto.request.DiscountProductRequestDto;
import solutions.skydev.pos.common.discount_service.dto.response.DiscountProductResponseDto;
import solutions.skydev.pos.discount_service.model.entity.Discount;
import solutions.skydev.pos.discount_service.model.entity.DiscountProduct;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiscountProductMapper {
    @Mapping(target = "discount", source = "discountId")
    DiscountProduct toEntity(DiscountProductRequestDto dto);

    default Discount map(Long discountId) {
        if (discountId == null) return null;
        Discount discount = new Discount();
        discount.setId(discountId);
        return discount;
    }

    @Mapping(source = "discount.id", target = "discountId")
    DiscountProductResponseDto toDto(DiscountProduct discountProduct);
    List<DiscountProductResponseDto> toDto(List<DiscountProduct> discountProductList);
}
