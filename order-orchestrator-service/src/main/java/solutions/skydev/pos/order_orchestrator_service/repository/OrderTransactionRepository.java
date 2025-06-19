package solutions.skydev.pos.order_orchestrator_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solutions.skydev.pos.order_orchestrator_service.model.entity.OrderTransaction;

import java.util.List;

@Repository
public interface OrderTransactionRepository extends JpaRepository<OrderTransaction, Long> {
    List<OrderTransaction> findByOrderId(Long orderId);
}
