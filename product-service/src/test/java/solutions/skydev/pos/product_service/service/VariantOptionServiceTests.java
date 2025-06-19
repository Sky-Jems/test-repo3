package solutions.skydev.pos.product_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import solutions.skydev.pos.product_service.model.entity.VariantOption;
import solutions.skydev.pos.product_service.model.entity.Product;
import solutions.skydev.pos.product_service.repository.VariantOptionRepository;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class VariantOptionServiceTests {
    @Mock
    private VariantOptionRepository variantOptionRepository;

    @Mock
    private VariantOptionServiceImpl variantOptionServiceImpl;

    @InjectMocks
    private VariantOptionServiceImpl variantOptionService;

    @Test
    @DisplayName("Should call repository save method")
    void save() {
       // Given
        Product product = new Product();
        product.setId(1L);
        VariantOption variantOption = new VariantOption();
        variantOption.setProduct(product);
        variantOption.setName("Test Variant Option");

        // When
        variantOptionService.addVariantOption(product, variantOption);

        // Then
        verify(variantOptionRepository).save(variantOption);
    }

    @Test
    @DisplayName("Should call repository findById method")
    void findById() {
        // Given
        Long productId = 1L;

        // When
        variantOptionService.getVariantOption(productId);

        // Then
        verify(variantOptionRepository).findById(productId);
    }

    @Test
    @DisplayName("Should call repository update method")
    void update() {
        // Given
        Long productId = 1L;
        VariantOption variantOption = new VariantOption();
        variantOption.setName("Updated Variant Option");

        // When
        variantOptionService.updateVariantOption(productId, variantOption);

        // Then
        verify(variantOptionRepository).save(variantOption);
    }

    @Test
    @DisplayName("Should call repository delete method")
    void delete() {
        // Given
        Long variantOptionId = 1L;

        // When
        variantOptionService.deleteVariantOption(variantOptionId);

        // Then
        verify(variantOptionRepository).deleteById(variantOptionId);
    }
}
