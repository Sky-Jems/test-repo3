package solutions.skydev.pos.product_service.service;

import solutions.skydev.pos.product_service.model.dto.response.ProductVariantAssignmentResponseDto;
import solutions.skydev.pos.product_service.model.entity.ProductVariantAssignment;

import java.util.List;

public interface ProductVariantAssignmentService {
    List<ProductVariantAssignment> getAllProductVariantAssignments();
    List<ProductVariantAssignment> getProductVariantAssignmentsByProductId(Long productId);
    List<ProductVariantAssignment> getProductVariantAssignmentsByVariantId(Long variantId);
}
