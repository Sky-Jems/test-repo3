package solutions.skydev.pos.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.product_service.model.entity.VariantOptionValue;

import java.util.List;

public interface VariantOptionValueRepository extends JpaRepository<VariantOptionValue, Long> {
    List<VariantOptionValue> findVariantOptionValuesByVariantOptionId(Long productId);
}
