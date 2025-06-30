package solutions.skydev.pos.discount_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.discount_service.model.entity.OrderLevelDiscountOrder;

import java.util.Optional;

public interface OrderLevelDiscountOrderRepository extends JpaRepository<OrderLevelDiscountOrder, Long> {
    Optional<OrderLevelDiscountOrder> findByOrderId(Long orderId);
}
