package solutions.skydev.pos.discount_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.discount_service.model.entity.DiscountLineItem;

import java.util.List;

public interface DiscountLineItemRepository extends JpaRepository<DiscountLineItem, Long> {
    List<DiscountLineItem> findAllByOrderId(Long orderId);
    DiscountLineItem findByOrderId(Long orderId);
}
