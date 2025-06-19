package solutions.skydev.pos.product_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solutions.skydev.pos.product_service.model.dto.response.CategoryResponseDto;
import solutions.skydev.pos.product_service.model.dto.response.ProductResponseDto;
import solutions.skydev.pos.product_service.model.entity.Category;
import solutions.skydev.pos.product_service.model.entity.Product;
import solutions.skydev.pos.product_service.model.mapper.CategoryMapper;
import solutions.skydev.pos.product_service.model.mapper.ProductMapper;
import solutions.skydev.pos.product_service.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    
    @Autowired
    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper, ProductMapper productMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
    }
    
    
    @GetMapping
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        return categoryMapper.toResponseDtoList(categories);
    }

    @GetMapping("/{id}/products")
    public List<ProductResponseDto> getProductsByCategory(@PathVariable("id") Long id) {
        // TODO: handle error when category does not exist
        List<Product> products = categoryService.getProductsByCategoryId(id);
        return productMapper.toResponseDtoList(products);
    }
}
