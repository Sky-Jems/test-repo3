package solutions.skydev.pos.discount_service.service.strategy.scope;

import solutions.skydev.pos.discount_service.model.entity.DiscountOrder;
import solutions.skydev.pos.discount_service.model.entity.Order;

import java.math.BigDecimal;

public interface DiscountScopeStrategy {
    BigDecimal applyDiscount(Order order, DiscountOrder discountOrder);
}
