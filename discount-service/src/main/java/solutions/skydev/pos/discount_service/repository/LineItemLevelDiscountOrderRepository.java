package solutions.skydev.pos.discount_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.discount_service.model.entity.LineItemLevelDiscountOrder;

import java.util.List;
import java.util.Optional;

public interface LineItemLevelDiscountOrderRepository extends JpaRepository<LineItemLevelDiscountOrder, Long> {
    Optional<LineItemLevelDiscountOrder> findByOrderIdAndLineItemId(Long orderId, Long lineItemId);
    List<LineItemLevelDiscountOrder> findAllByOrderIdAndLineItemId(Long orderId, Long lineItemId);
}
