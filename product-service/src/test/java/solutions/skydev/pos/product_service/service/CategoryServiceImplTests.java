package solutions.skydev.pos.product_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import solutions.skydev.pos.product_service.model.entity.Category;
import solutions.skydev.pos.product_service.repository.CategoryRepository;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTests {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Should call repository save method")
    void addCategory() {
        // Given
        Category category = new Category();
        category.setName("Test Category");

        // When
        categoryService.addCategory(category);

        // Then
        verify(categoryRepository).save(category);
    }

    @Test
    @DisplayName("Should call repository findById method")
    void getCategoryById() {
        // Given
        Long categoryId = 1L;

        // When
        categoryService.getCategoryById(categoryId);

        // Then
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    @DisplayName("Should call repository update method")
    void updateCategory() {
        // Given
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Updated Category");

        // When
        categoryService.updateCategory(categoryId, category);

        // Then
        verify(categoryRepository).save(category);
    }

    @Test
    @DisplayName("Should call repository deleteById method")
    void deleteCategory() {
        // Given
        Long categoryId = 1L;

        // When
        categoryService.deleteCategory(categoryId);

        // Then
        verify(categoryRepository).deleteById(categoryId);
    }
}
