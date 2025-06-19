package solutions.skydev.pos.product_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import solutions.skydev.pos.product_service.model.entity.*;
import solutions.skydev.pos.product_service.repository.ProductVariantAssignmentRepository;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductVariantAssignmentTests {
    @Mock
    private ProductVariantAssignmentRepository productVariantAssignmentRepository;

    @InjectMocks
    private ProductVariantAssignmentImpl productVariantAssignmentService;

    @Test
    @DisplayName("Should call repository save method")
    void save() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("This is a test product");
        Variant variant = new Variant();
        variant.setId(2L);
        variant.setProduct(product);

        VariantOption variantOption = new VariantOption();
        variantOption.setId(3L);
        variantOption.setName("Size");
        VariantOptionValue variantOptionValue = new VariantOptionValue();
        variantOptionValue.setId(4L);
        variantOptionValue.setVariantOption(variantOption);

        ProductVariantAssignment productVariantAssignment = new ProductVariantAssignment();
        productVariantAssignment.setVariant(variant);
        productVariantAssignment.setVariantOptionValue(variantOptionValue);

        // When
        productVariantAssignmentService.addProductVariantAssignment(productVariantAssignment);

        // Then
        verify(productVariantAssignmentRepository).save(productVariantAssignment);
    }

    @Test
    @DisplayName("Should call repository findById method")
    void findById() {
        Long productVariantAssignmentId = 1L;

        // When
        productVariantAssignmentService.getProductVariantAssignmentById(productVariantAssignmentId);

        // Then
        verify(productVariantAssignmentRepository).findById(productVariantAssignmentId);
    }

    @Test
    @DisplayName("Should call repository update method")
    void update() {
        Long productVariantAssignmentId = 1L;
        ProductVariantAssignment productVariantAssignment = new ProductVariantAssignment();
        productVariantAssignment.setId(productVariantAssignmentId);
        productVariantAssignment.setVariant(new Variant());
        productVariantAssignment.setVariantOptionValue(new VariantOptionValue());

        // When
        productVariantAssignmentService.updateProductVariantAssignment(productVariantAssignment);

        // Then
        verify(productVariantAssignmentRepository).save(productVariantAssignment);
    }

    @Test
    @DisplayName("Should call repository delete method")
    void delete() {
        Long productVariantAssignmentId = 1L;

        // When
        productVariantAssignmentService.deleteProductVariantAssignment(productVariantAssignmentId);

        // Then
        verify(productVariantAssignmentRepository).deleteById(productVariantAssignmentId);
    }
}
