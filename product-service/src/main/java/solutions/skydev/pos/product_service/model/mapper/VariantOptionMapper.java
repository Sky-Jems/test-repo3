package solutions.skydev.pos.product_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import solutions.skydev.pos.product_service.model.dto.request.VariantOptionRequestDto;
import solutions.skydev.pos.product_service.model.dto.response.VariantOptionResponseDto;
import solutions.skydev.pos.product_service.model.entity.VariantOption;
import solutions.skydev.pos.product_service.model.entity.VariantOptionValue;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Mapper(componentModel = "spring")
public interface VariantOptionMapper {
    @Mapping(target = "values", source = "values", qualifiedByName = "mapVariantValuesToValues")
    @Mapping(target = "product.id", source = "productId")
    VariantOption toEntity(VariantOptionRequestDto variantOptionRequestDto);

    List<VariantOption> toEntityList(List<VariantOptionRequestDto> variantOptionRequestDtoList);

    @Mapping(target = "productId", source = "product.id")
    VariantOptionResponseDto toResponseDto(VariantOption variantOption);
    List<VariantOptionResponseDto> toResponseDtoList(List<VariantOption> variantOptions);

    @Named("mapVariantValuesToValues")
    default Set<VariantOptionValue> mapVariantValuesToValues(Set<VariantOptionValue> values) {
        Set<VariantOptionValue> variantOptionValues = new HashSet<>(Set.of());
        values.forEach(value -> {
            VariantOptionValue variantOptionValue = new VariantOptionValue();
            variantOptionValue.setId(value.getId());
            variantOptionValue.setValue(value.getValue());
            variantOptionValues.add(variantOptionValue);
        });
        return variantOptionValues;
    }
}
