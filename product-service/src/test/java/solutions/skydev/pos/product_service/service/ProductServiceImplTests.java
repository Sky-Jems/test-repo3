package solutions.skydev.pos.product_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import solutions.skydev.pos.product_service.model.entity.Product;
import solutions.skydev.pos.product_service.producer.ProductProducer;
import solutions.skydev.pos.product_service.repository.ProductRepository;

import java.util.Optional;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTests {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductProducer productProducer;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("Should call repository save method")
    void save() {
        // Given
        Product product = new Product();
        product.setDescription("This is a sample product");
        product.setName("Test Product");

        // When
        Product createdProduct = productService.save(product);

        // Then
        verify(productRepository).save(product);
        verify(productProducer).sendProductCreated(createdProduct);
    }

    @Test
    @DisplayName("Should call repository findById method")
    void findById() {
        // Given
        Long productId = 1L;

        // When
        productService.findById(productId);

        // Then
        verify(productRepository).findById(productId);
    }

    @Test
    @DisplayName("Should call repository update method")
    void update() {
        // Given
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setDescription("Updated description");
        product.setName("Updated Product");

        // When
        Product updatedProduct = productService.update(productId, product);

        // Then
        verify(productRepository).save(product);
        verify(productProducer).sendProductUpdated(updatedProduct);
    }

    @Test
    @DisplayName("Should call repository deleteById method")
    void deleteById() {
        // Given
        Long productId = 1L;

        // When
        Optional<Product> product = productRepository.findById(productId);
        productService.deleteById(productId);

        // Then
        verify(productRepository).deleteById(productId);
        verify(productProducer).sendProductDeleted(product);
    }
}
