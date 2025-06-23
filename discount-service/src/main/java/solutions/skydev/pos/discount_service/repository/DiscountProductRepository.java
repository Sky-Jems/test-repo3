package solutions.skydev.pos.discount_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.discount_service.model.entity.DiscountProduct;

import java.util.List;

public interface DiscountProductRepository extends JpaRepository<DiscountProduct, Long> {
    List<DiscountProduct> findAllByDiscount_Id(Long discountId);
}
