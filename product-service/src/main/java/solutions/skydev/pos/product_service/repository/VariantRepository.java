package solutions.skydev.pos.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import solutions.skydev.pos.product_service.model.entity.Variant;

import java.util.List;

public interface VariantRepository extends JpaRepository<Variant, Long> {
    List<Variant> findByProductId(Long productId);

    @Query("""
        SELECT DISTINCT v FROM Variant v
        JOIN ProductVariantAssignment pva ON pva.variant = v
        JOIN VariantOptionValue vov ON pva.variantOptionValue = vov
        JOIN VariantOption vo ON vov.variantOption = vo
        WHERE v.product.id = :productId
          AND (:variantOptionId IS NULL OR vo.id = :variantOptionId)
    """)
    List<Variant> findByProductIdAndVariantOptionId(
            @Param("productId") Long productId,
            @Param("variantOptionId") Long variantOptionId
    );

    @Query("""
        SELECT DISTINCT v FROM Variant v
        JOIN ProductVariantAssignment pva ON pva.variant = v
        JOIN VariantOptionValue vov ON pva.variantOptionValue = vov
        WHERE v.product.id = :productId
          AND (:valueIds IS NULL OR vov.id IN :valueIds)
    """)
    List<Variant> findByProductIdAndVariantOptionValueIds(
            @Param("productId") Long productId,
            @Param("valueIds") List<Long> valueIds
    );
}
