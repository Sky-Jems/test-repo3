package solutions.skydev.pos.product_service.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.common.error.domain.ConflictException;
import solutions.skydev.pos.common.error.domain.ResourceNotFoundException;
import solutions.skydev.pos.common.error.domain.ValidationException;
import solutions.skydev.pos.product_service.model.entity.Category;
import solutions.skydev.pos.product_service.model.entity.Product;
import solutions.skydev.pos.product_service.repository.CategoryRepository;
import solutions.skydev.pos.product_service.repository.ProductRepository;

import java.util.Comparator;
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
        if (category.getName() == null || category.getName().isEmpty()) {
            throw new ValidationException("Category name cannot be empty");
        }

        // Capitalize the first letter of the category name
        String capitalizedCategoryName = category.getName().substring(0, 1).toUpperCase() + category.getName().substring(1);
        category.setName(capitalizedCategoryName);

        if (category.getName().length() > 50 || category.getName().length() < 3) {
            throw new ValidationException("Category name must be between 3 and 50 characters long");
        }

        if (categoryRepository.existsByName((category.getName()))) {
            throw new ConflictException("Category with name '" + category.getName() + "' already exists");
        }

        // Should not container numbers or special characters
        if (!category.getName().matches("^[a-zA-Z ]+$")) {
            throw new ValidationException("Category name can only contain letters and spaces");
        }

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
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }

        if (updatedCategory.getName() == null || updatedCategory.getName().isEmpty()) {
            throw new ValidationException("Category name cannot be empty");
        }

        String capitalizedCategoryName = updatedCategory.getName().substring(0, 1).toUpperCase() + updatedCategory.getName().substring(1);
        updatedCategory.setName(capitalizedCategoryName);


        if (updatedCategory.getName().length() > 50 || updatedCategory.getName().length() < 3) {
            throw new ValidationException("Category name must be between 3 and 50 characters long");
        }

        if (categoryRepository.existsByName(updatedCategory.getName()) && !updatedCategory.getName().equals(existingCategory.getName())) {
            throw new ConflictException("Category with name '" + updatedCategory.getName() + "' already exists");
        }

        if (!updatedCategory.getName().matches("^[a-zA-Z ]+$")) {
            throw new ValidationException("Category name can only contain letters and spaces");
        }

        return categoryRepository.save(updatedCategory);
    }

    @Override
    @Transactional
    public Category deleteCategory(Long id) {
        Category existingCategory = categoryRepository.findById(id).orElse(null);
        if (existingCategory == null) {
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }

        // Check if the category has products associated with it
        if (!existingCategory.getProducts().isEmpty()) {
            throw new ConflictException("Category with id " + id + " cannot be deleted because it has products associated with it");
        }

        categoryRepository.deleteById(id);
        return existingCategory;
    }

    public List<Category> findAll() {
        return this.categoryRepository.findAll(Sort.by("name").ascending());
    }

    @Transactional
    public Product addProduct(Category category, Product product) {
        // add product to many_to_many relation of category.products
        category = this.categoryRepository.findById(category.getId()).orElse(null);
        product = this.productRepository.findById(product.getId()).orElse(null);
        // TODO: handle error when category or product does not exist

        category.getProducts().add(product);
        this.categoryRepository.save(category);

//        product.getCategories().add(category);
//        this.productRepository.save(product);
//        // save category to persist the changes
        return product;
    }

    @Override
    public List<Product> getProductsByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        return category != null ? category.getProducts()
                .stream()
                .sorted(Comparator.comparing(p -> p.getName().toLowerCase()))
                .toList() : null;
    }
}
