package solutions.skydev.pos.discount_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.discount_service.model.entity.DiscountVariant;

import java.util.List;

public interface DiscountVariantRepository extends JpaRepository<DiscountVariant, Long> {
    List<DiscountVariant> findAllByDiscount_Id(Long discountId);
}
