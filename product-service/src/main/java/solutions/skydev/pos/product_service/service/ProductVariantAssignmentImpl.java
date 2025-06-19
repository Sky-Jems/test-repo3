package solutions.skydev.pos.product_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.product_service.model.entity.ProductVariantAssignment;
import solutions.skydev.pos.product_service.repository.ProductVariantAssignmentRepository;

@Service
public class ProductVariantAssignmentImpl {
    private final ProductVariantAssignmentRepository productVariantAssignmentRepository;

    @Autowired
    public ProductVariantAssignmentImpl(ProductVariantAssignmentRepository productVariantAssignmentRepository) {
        this.productVariantAssignmentRepository = productVariantAssignmentRepository;
    }

    public ProductVariantAssignment addProductVariantAssignment(ProductVariantAssignment productVariantAssignment) {
        return productVariantAssignmentRepository.save(productVariantAssignment);
    }

    public ProductVariantAssignment getProductVariantAssignmentById(Long id) {
        return productVariantAssignmentRepository.findById(id).orElse(null);
    }

    public ProductVariantAssignment updateProductVariantAssignment(ProductVariantAssignment productVariantAssignment) {
        return productVariantAssignmentRepository.save(productVariantAssignment);
    }

    public void deleteProductVariantAssignment(Long id) {
        productVariantAssignmentRepository.deleteById(id);
    }
}
