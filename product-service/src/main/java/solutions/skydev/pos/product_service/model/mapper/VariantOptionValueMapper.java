package solutions.skydev.pos.product_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import solutions.skydev.pos.product_service.model.dto.request.VariantOptionValueRequestDto;
import solutions.skydev.pos.product_service.model.dto.response.VariantOptionValueResponseDto;
import solutions.skydev.pos.product_service.model.entity.VariantOptionValue;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VariantOptionValueMapper {
    @Mapping(source = "variantOption.id", target = "variantOptionId")
    VariantOptionValueResponseDto toDto(VariantOptionValue variantOptionValue);
    List<VariantOptionValueResponseDto> toDtoList(List<VariantOptionValue> variantOptionValues);
    @Mapping(source = "variantOptionId", target = "variantOption.id")
    VariantOptionValue toEntity(VariantOptionValueRequestDto variantOptionValueRequestDto);
}
