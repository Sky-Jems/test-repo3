package solutions.skydev.pos.discount_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.discount_service.model.entity.Discount;
import solutions.skydev.pos.discount_service.model.entity.DiscountOrder;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findByName(String name);
}
