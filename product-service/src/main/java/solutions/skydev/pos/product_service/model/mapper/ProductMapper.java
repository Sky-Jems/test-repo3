package solutions.skydev.pos.product_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import solutions.skydev.pos.common.product_service.dto.request.CategoryProductRequestDto;
import solutions.skydev.pos.common.product_service.dto.request.ProductRequestDto;
import solutions.skydev.pos.common.product_service.dto.response.ProductResponseDto;
import solutions.skydev.pos.product_service.model.entity.Product;
import solutions.skydev.pos.product_service.model.entity.Category;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    
     @Mapping(target = "categoryIds", source = "categories", qualifiedByName = "mapCategoriesToCategoryIds")
     ProductResponseDto toResponseDto(Product product);
     
     List<ProductResponseDto> toResponseDtoList(List<Product> products);
     
     @Mapping(target = "categories", source = "categoryIds", qualifiedByName = "mapCategoryIdToCategories")
     Product toEntity(ProductRequestDto productRequestDto);
     
     @Named("mapCategoriesToCategoryIds")
        default List<Long> mapCategoriesToCategoryIds(Set<Category> categories) {
            try {
                if (categories == null || categories.isEmpty()) {
                    return List.of();
                }
                return categories.stream()
                        .map(Category::getId)
                        .toList();
            } catch (Exception e) {
                System.out.println("Error mapping categories to category IDs: " + e.getMessage());
                return List.of();
            }
        }


     @Named("mapCategoryIdToCategories")
     default Set<Category> mapCategoryIdToCategory(List<Long> categoryIds) {
         if (categoryIds == null || categoryIds.isEmpty()) {
             return new HashSet<>();
         }
         Set<Category> categories = new HashSet<>();
         categoryIds.stream().map(id -> {
             Category category = new Category();
             category.setId(id);
             return category;
         }).forEach(categories::add);
        return categories;
     }
}
