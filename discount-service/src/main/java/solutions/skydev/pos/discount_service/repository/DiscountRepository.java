package solutions.skydev.pos.discount_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.discount_service.model.entity.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
