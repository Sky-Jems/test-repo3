package solutions.skydev.pos.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.product_service.model.entity.ProductVariantAssignment;

import java.util.List;

public interface ProductVariantAssignmentRepository extends JpaRepository<ProductVariantAssignment, Long> {
    List<ProductVariantAssignment> getProductsVariantAssignmentsByVariant_Product_Id(Long variantProductId);
    List<ProductVariantAssignment> getProductsVariantAssignmentsByVariant_Id(Long variantProductId);

    List<ProductVariantAssignment> findProductVariantAssignmentByVariant_Id(Long variantId);
}
