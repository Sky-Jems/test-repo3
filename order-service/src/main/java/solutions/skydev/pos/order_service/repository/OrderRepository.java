package solutions.skydev.pos.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.order_service.model.entity.Order;

import java.time.OffsetDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCreatedAtBetween(OffsetDateTime from, OffsetDateTime to);

}
