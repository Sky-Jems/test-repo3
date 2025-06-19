package solutions.skydev.pos.discount_service.service;
import solutions.skydev.pos.discount_service.model.entity.DiscountOrder;

public interface DiscountOrderService {
    DiscountOrder create(DiscountOrder DiscountOrder);
    DiscountOrder findByOrderId(Long id);
    DiscountOrder deleteByOrderId(Long id);
}
