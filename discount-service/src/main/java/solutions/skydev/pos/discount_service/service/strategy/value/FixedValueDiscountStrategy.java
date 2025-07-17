package solutions.skydev.pos.discount_service.service.strategy.value;

import java.math.BigDecimal;

public class FixedValueDiscountStrategy implements DiscountValueStrategy {

    private final BigDecimal value;

    public FixedValueDiscountStrategy(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal calculate(BigDecimal originalAmount) {
        return value.min(originalAmount);
    }
}
