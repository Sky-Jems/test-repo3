package solutions.skydev.pos.product_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import solutions.skydev.pos.product_service.model.entity.Product;
import solutions.skydev.pos.product_service.model.entity.Variant;
import solutions.skydev.pos.product_service.repository.VariantRepository;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class VariantServiceTests {
    @Mock
    private VariantRepository variantRepository;

    @InjectMocks
    private VariantServiceImpl variantService;

    @Test
    @DisplayName("Should call repository save method")
    void save() {
        // Given
        Variant variant = new Variant();
        variant.setProduct(new Product());

        // When
        variantService.addVariant(variant);

        // Then
        verify(variantRepository).save(variant);
    }

    @Test
    @DisplayName("Should call repository findById method")
    void findById() {
        // Given
        Long variantId = 1L;

        // When
        variantService.getVariantById(variantId);

        // Then
        verify(variantRepository).findById(variantId);
    }

    @Test
    @DisplayName("Should call repository update method")
    void update() {
        // Given
        Long variantId = 1L;
        Variant variant = new Variant();
        variant.setId(variantId);
        variant.setProduct(new Product());

        // When
        variantService.updateVariant(variantId, variant);

        // Then
        verify(variantRepository).save(variant);
    }

    @Test
    @DisplayName("Should call repository delete method")
    void delete() {
        // Given
        Long variantId = 1L;

        // When
        variantService.deleteVariant(variantId);

        // Then
        verify(variantRepository).deleteById(variantId);
    }
}
