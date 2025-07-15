package solutions.skydev.pos.discount_service.service.strategy.value;

import java.math.BigDecimal;

public class PercentageDiscountStrategy implements DiscountValueStrategy {

    private final BigDecimal percentage;

    public PercentageDiscountStrategy(BigDecimal percentage) {
        this.percentage = percentage;
    }

    @Override
    public BigDecimal calculate(BigDecimal originalAmount) {
        return originalAmount.multiply(percentage).divide(BigDecimal.valueOf(100));
    }
}


