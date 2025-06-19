package solutions.skydev.pos.discount_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.discount_service.model.entity.DiscountOrder;

public interface DiscountOrderRepository extends JpaRepository<DiscountOrder, Long> {
    DiscountOrder findByOrderId(Long orderId);
}
