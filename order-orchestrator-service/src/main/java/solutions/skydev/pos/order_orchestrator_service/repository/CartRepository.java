package solutions.skydev.pos.order_orchestrator_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.order_orchestrator_service.model.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> { }
