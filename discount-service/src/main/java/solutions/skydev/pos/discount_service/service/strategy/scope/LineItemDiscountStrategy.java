package solutions.skydev.pos.discount_service.service.strategy.scope;

import solutions.skydev.pos.discount_service.model.entity.DiscountOrder;
import solutions.skydev.pos.discount_service.model.entity.LineItemLevelDiscountOrder;
import solutions.skydev.pos.discount_service.model.entity.Order;
import solutions.skydev.pos.discount_service.service.strategy.value.DiscountValueStrategy;

import java.math.BigDecimal;
import java.util.Objects;

public class LineItemDiscountStrategy implements DiscountScopeStrategy {

    private final DiscountValueStrategy discountValueStrategy;

    public LineItemDiscountStrategy(DiscountValueStrategy discountValueStrategy) {
        this.discountValueStrategy = discountValueStrategy;
    }

    @Override
    public BigDecimal applyDiscount(Order order, DiscountOrder discountOrder) {
        if (!(discountOrder instanceof LineItemLevelDiscountOrder lineItemOrder)) {
            throw new IllegalArgumentException("Expected LineItemLevelDiscountOrder");
        }

        if (lineItemOrder.getPrice() == null || lineItemOrder.getQuantity() == null) {
            throw new IllegalStateException("Line item missing price or quantity");
        }

        BigDecimal subTotal = lineItemOrder.getSubTotal();
        if (subTotal == null) {
            subTotal = lineItemOrder.getPrice().multiply(BigDecimal.valueOf(lineItemOrder.getQuantity()));
        }

        return discountValueStrategy.calculate(subTotal);
    }
}
