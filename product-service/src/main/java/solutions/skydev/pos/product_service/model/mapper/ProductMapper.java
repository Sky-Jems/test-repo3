package solutions.skydev.pos.product_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import solutions.skydev.pos.product_service.model.dto.request.CategoryProductRequestDto;
import solutions.skydev.pos.product_service.model.dto.request.ProductRequestDto;
import solutions.skydev.pos.product_service.model.dto.response.ProductResponseDto;
import solutions.skydev.pos.product_service.model.entity.Product;
import solutions.skydev.pos.product_service.model.entity.Category;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ProductMapper {
     ProductResponseDto toResponseDto(Product product);
     List<ProductResponseDto> toResponseDtoList(List<Product> products);
     @Mapping(target = "categories", source = "categories", qualifiedByName = "mapCategoryIdToCategory")
     Product toEntity(ProductRequestDto productRequestDto);
     @Mapping(source = "productId", target = "id")
     Product toEntity(CategoryProductRequestDto categoryProductRequestDto);


     @Named("mapCategoryIdToCategory")
     default Set<Category> mapCategoryIdToCategory(Optional<Set<Long>> categoryIds) {
          if (categoryIds == null) {
               return null;
          }
          Set<Category> categories = new HashSet<>(Set.of());
          categoryIds.get().forEach(categoryId -> {
               Category category = new Category();
               category.setId(categoryId);
               categories.add(category);
          });
          return categories;
     }
}

