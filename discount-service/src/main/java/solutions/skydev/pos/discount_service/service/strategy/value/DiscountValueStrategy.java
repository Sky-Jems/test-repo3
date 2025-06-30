package solutions.skydev.pos.discount_service.service.strategy.value;

import solutions.skydev.pos.discount_service.model.entity.Discount;
import solutions.skydev.pos.discount_service.model.entity.DiscountProduct;
import solutions.skydev.pos.discount_service.model.entity.Order;
import solutions.skydev.pos.discount_service.model.enums.DiscountType;

import java.math.BigDecimal;
import java.util.List;

public interface DiscountValueStrategy {
    BigDecimal calculate(BigDecimal amount);
}
