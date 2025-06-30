package solutions.skydev.pos.discount_service.service.strategy.scope;

import java.math.BigDecimal;

import solutions.skydev.pos.discount_service.model.entity.DiscountOrder;
import solutions.skydev.pos.discount_service.model.entity.Order;
import solutions.skydev.pos.discount_service.service.strategy.value.DiscountValueStrategy;

public class OrderLevelDiscountStrategy implements DiscountScopeStrategy {

    private final DiscountValueStrategy discountValueStrategy;

    public OrderLevelDiscountStrategy(DiscountValueStrategy discountValueStrategy) {
        this.discountValueStrategy = discountValueStrategy;
    }

    @Override
    public BigDecimal applyDiscount(Order order, DiscountOrder discountOrder) {
        BigDecimal total = BigDecimal.valueOf(order.getTotalAmount());
        return discountValueStrategy.calculate(total);
    }
}
