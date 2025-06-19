package solutions.skydev.pos.product_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.product_service.model.dto.response.ProductVariantAssignmentResponseDto;
import solutions.skydev.pos.product_service.model.entity.ProductVariantAssignment;
import solutions.skydev.pos.product_service.model.mapper.ProductVariantAssignmentMapper;
import solutions.skydev.pos.product_service.repository.ProductVariantAssignmentRepository;

import java.util.List;

@Service
public class ProductVariantAssignmentServiceImpl implements ProductVariantAssignmentService {
    private final ProductVariantAssignmentRepository productVariantAssignmentRepository;
    private final ProductVariantAssignmentMapper productVariantAssignmentMapper;

    @Autowired
    public ProductVariantAssignmentServiceImpl(
            ProductVariantAssignmentRepository productVariantAssignmentRepository,
            ProductVariantAssignmentMapper productVariantAssignmentMapper) {
        this.productVariantAssignmentRepository = productVariantAssignmentRepository;
        this.productVariantAssignmentMapper = productVariantAssignmentMapper;
    }
    
    public List<ProductVariantAssignment> getAllProductVariantAssignments() {
        return productVariantAssignmentRepository.findAll();
    }

    public List<ProductVariantAssignment> getProductVariantAssignmentsByProductId(Long productId) {
        return productVariantAssignmentRepository.getProductsVariantAssignmentsByVariant_Product_Id(productId);
    }
    
    public List<ProductVariantAssignment> getProductVariantAssignmentsByVariantId(Long variantId) {
        return productVariantAssignmentRepository.findProductVariantAssignmentByVariant_Id(variantId);
    }
}
