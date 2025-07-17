package solutions.skydev.pos.product_service.service;

import solutions.skydev.pos.product_service.model.entity.Category;
import solutions.skydev.pos.product_service.model.entity.Product;

import java.util.List;

public interface CategoryService {
    Category addCategory(Category category);
    Category getCategoryById(Long id);
    Category updateCategory(Long id, Category category);
    Product addProduct(Category category, Product product);
    Category deleteCategory(Long id);
    List<Category> findAll();
    List<Product> getProductsByCategoryId(Long categoryId);
}
