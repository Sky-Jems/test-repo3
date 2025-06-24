package solutions.skydev.pos.product_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import solutions.skydev.pos.common.product_service.dto.request.CategoryProductRequestDto;
import solutions.skydev.pos.common.product_service.dto.request.CategoryRequestDto;
import solutions.skydev.pos.common.product_service.dto.response.CategoryResponseDto;
import solutions.skydev.pos.product_service.model.entity.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryRequestDto toDto(Category category);
    Category toEntity(CategoryRequestDto categoryRequestDto);
    CategoryResponseDto toResponseDto(Category category);
    List<CategoryResponseDto> toResponseDtoList(List<Category> categories);
    @Mapping(source = "categoryId", target = "id")
    Category toEntity(CategoryProductRequestDto categoryProductRequestDto);
}
