package solutions.skydev.pos.product_service.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.product_service.model.entity.Category;
import solutions.skydev.pos.product_service.model.entity.Product;
import solutions.skydev.pos.product_service.repository.CategoryRepository;
import solutions.skydev.pos.product_service.repository.ProductRepository;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category updateCategory(Long id, Category updatedCategory) {
        Category existingCategory = categoryRepository.findById(id).orElse(null);
        if (existingCategory == null) {
            // TODO: handle the case where the product doesn't exist.
            return null;
        }
        return categoryRepository.save(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
    
    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    @Transactional
    public Product addProduct(Category category, Product product) {
        // add product to many_to_many relation of category.products
        category = this.categoryRepository.findById(category.getId()).orElse(null);
        product = this.productRepository.findById(product.getId()).orElse(null);
        // TODO: handle error when category or product does not exist

        category.getProducts().add(product);
        this.categoryRepository.save(category);

        product.getCategories().add(category);
        this.productRepository.save(product);
        // save category to persist the changes
        return product;
    }

    @Override
    public List<Product> getProductsByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        return category != null ? category.getProducts().stream().toList() : null;
    }
}
