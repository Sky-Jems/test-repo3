package solutions.skydev.pos.product_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import solutions.skydev.pos.product_service.model.dto.request.VariantRequestDto;
import solutions.skydev.pos.product_service.model.entity.Variant;
import solutions.skydev.pos.product_service.model.dto.response.VariantResponseDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VariantMapper {
    @Mapping(source = "id", target = "variantId")
    VariantResponseDto toResponseDto(Variant variant);
    List<VariantResponseDto> toResponseDtoList(List<Variant> variants);

    @Mapping(source = "productId", target = "product.id")
    Variant toEntity(VariantRequestDto variantRequestDto);
}
