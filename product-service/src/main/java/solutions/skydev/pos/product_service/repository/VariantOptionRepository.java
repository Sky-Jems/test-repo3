package solutions.skydev.pos.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.product_service.model.entity.VariantOption;

import java.util.List;

public interface VariantOptionRepository extends JpaRepository<VariantOption, Long> {
    List<VariantOption> findAllByProductId(Long productId);
}
