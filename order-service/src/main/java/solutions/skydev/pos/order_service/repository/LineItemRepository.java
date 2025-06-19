package solutions.skydev.pos.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.order_service.model.entity.LineItem;

public interface LineItemRepository extends JpaRepository<LineItem, Long> { }
