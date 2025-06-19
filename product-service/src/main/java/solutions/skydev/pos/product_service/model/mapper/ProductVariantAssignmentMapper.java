package solutions.skydev.pos.product_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import solutions.skydev.pos.product_service.model.dto.response.ProductVariantAssignmentResponseDto;
import solutions.skydev.pos.product_service.model.entity.ProductVariantAssignment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductVariantAssignmentMapper {
    @Mapping(source = "variant.id", target = "variantId")
    @Mapping(source = "variantOptionValue.id", target = "variantOptionValueId")
    ProductVariantAssignmentResponseDto toResponseDto(ProductVariantAssignment productVariantAssignment);
    
    List<ProductVariantAssignmentResponseDto> toResponseDtoList(List<ProductVariantAssignment> productVariantAssignments);
}