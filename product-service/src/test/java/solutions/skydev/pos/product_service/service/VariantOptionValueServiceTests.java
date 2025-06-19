package solutions.skydev.pos.product_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import solutions.skydev.pos.product_service.model.entity.VariantOption;
import solutions.skydev.pos.product_service.model.entity.VariantOptionValue;
import solutions.skydev.pos.product_service.repository.VariantOptionValueRepository;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class VariantOptionValueServiceTests {
     @Mock
     private VariantOptionValueRepository variantOptionValueRepository;

     @Mock
     private VariantOptionValueImpl variantOptionValueServiceImpl;

     @InjectMocks
     private VariantOptionValueImpl variantOptionValueService;

     // Error pls fix
//     @Test
//     @DisplayName("Should call repository save method")
//     void save() {
//         // Given
//         VariantOption variantOption = new VariantOption();
//         variantOption.setName("Size");
//         VariantOptionValue variantOptionValue = new VariantOptionValue();
//         variantOptionValue.setVariantOption(variantOption);
//         variantOptionValue.setValue("Large");
//
//        // When
//        variantOptionValueService.addVariantOptionValue(variantOptionValue);
//
//        // Then
//        verify(variantOptionValueRepository).save(variantOptionValue);
//     }

    @Test
    @DisplayName("Should call repository findById method")
    void findById() {
        // Given
        Long variantOptionValueId = 1L;

        // When
        variantOptionValueService.getVariantOptionValueById(variantOptionValueId);

        // Then
        verify(variantOptionValueRepository).findById(variantOptionValueId);
    }

    @Test
    @DisplayName("Should call repository update method")
    void update() {
        // Given
        Long variantOptionValueId = 1L;
        VariantOptionValue variantOptionValue = new VariantOptionValue();
        variantOptionValue.setId(variantOptionValueId);
        variantOptionValue.setValue("Updated Value");

        // When
        variantOptionValueService.updateVariantOptionValue(variantOptionValueId, variantOptionValue);

        // Then
        verify(variantOptionValueRepository).save(variantOptionValue);
    }

    @Test
    @DisplayName("Should call repository delete method")
    void delete() {
        // Given
        Long variantOptionValueId = 1L;

        // When
        variantOptionValueService.deleteVariantOptionValue(variantOptionValueId);

        // Then
        verify(variantOptionValueRepository).deleteById(variantOptionValueId);
    }
}
