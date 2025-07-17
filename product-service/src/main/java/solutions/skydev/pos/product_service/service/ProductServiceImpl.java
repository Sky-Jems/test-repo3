package solutions.skydev.pos.product_service.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solutions.skydev.pos.common.error.domain.ValidationException;
import solutions.skydev.pos.product_service.model.entity.Product;
import solutions.skydev.pos.product_service.repository.CategoryRepository;
import solutions.skydev.pos.product_service.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Product save(Product product) {
        // Name
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new ValidationException("Product name cannot be empty");
        }
        
        if (product.getName().length() > 250 || product.getName().length() < 3) {
            throw new ValidationException("Product name must be between 3 and 250 characters long");
        }
        
        if (productRepository.existsByName(product.getName())) {
            throw new ValidationException("Product with name '" + product.getName() + "' already exists");
        }
        
        // Price
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Product price must be greater than zero");
        }
        
        if (product.getPrice().scale() > 2) {
            throw new ValidationException("Product price can have a maximum of two decimal places");
        }
        
        // Description
        if (product.getDescription() != null && product.getDescription().length() > 500) {
            throw new ValidationException("Product description cannot exceed 500 characters");
        }
        
        // Category
        if (product.getCategories() == null || product.getCategories().isEmpty()) {
            throw new ValidationException("Product must have at least one category");
        }

        if (product.getCategories().stream().allMatch(category -> 
                category.getId() == null || !categoryRepository.existsById(category.getId()))) {
            throw new ValidationException("Product must have valid categories");
        }
        
        
        return productRepository.save(product);
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product update(Product productToUpdate) {
        Product existingProduct = productRepository.findById(productToUpdate.getId())
                .orElseThrow(() -> new ValidationException("Product with ID " + productToUpdate.getId() + " does not exist"));

        // TODO: duplicated in create method, refactor: find a way for better code reuse
        if (productToUpdate.getName() == null || productToUpdate.getName().isEmpty()) {
            throw new ValidationException("Product name cannot be empty");
        }

        if (productToUpdate.getName().length() > 250 || productToUpdate.getName().length() < 3) {
            throw new ValidationException("Product name must be between 3 and 250 characters long");
        }

        if (productRepository.existsByName(productToUpdate.getName())) {
            throw new ValidationException("Product with name '" + productToUpdate.getName() + "' already exists");
        }

        // Price
        if (productToUpdate.getPrice() == null || productToUpdate.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Product price must be greater than zero");
        }

        if (productToUpdate.getPrice().scale() > 2) {
            throw new ValidationException("Product price can have a maximum of two decimal places");
        }
        
        // Description
        if (productToUpdate.getDescription() != null && productToUpdate.getDescription().length() > 500) {
            throw new ValidationException("Product description cannot exceed 500 characters");
        }


        // Category
        if (productToUpdate.getCategories() == null || productToUpdate.getCategories().isEmpty()) {
            throw new ValidationException("Product must have at least one category");
        }

        if (productToUpdate.getCategories().stream().allMatch(category ->
                category.getId() == null || !categoryRepository.existsById(category.getId()))) {
            throw new ValidationException("Product must have valid categories");
        }
        
        return productRepository.save(productToUpdate);
    }

    @Transactional
    public Product deleteById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        productRepository.deleteById(id);
        return product;
    }

    public List<Product> findAll() {
        return productRepository.findAll(Sort.by("name").ascending());
    }
}
