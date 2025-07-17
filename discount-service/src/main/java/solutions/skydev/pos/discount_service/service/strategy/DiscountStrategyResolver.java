package solutions.skydev.pos.discount_service.service.strategy;

import org.springframework.stereotype.Component;
import solutions.skydev.pos.discount_service.model.enums.DiscountScope;
import solutions.skydev.pos.discount_service.model.enums.DiscountType;
//import solutions.skydev.pos.discount_service.service.strategy.scope.LineItemDiscountStrategy;
import solutions.skydev.pos.discount_service.service.strategy.scope.LineItemDiscountStrategy;
import solutions.skydev.pos.discount_service.service.strategy.scope.OrderLevelDiscountStrategy;
import solutions.skydev.pos.discount_service.service.strategy.scope.DiscountScopeStrategy;
import solutions.skydev.pos.discount_service.service.strategy.value.FixedValueDiscountStrategy;
import solutions.skydev.pos.discount_service.service.strategy.value.PercentageDiscountStrategy;
import solutions.skydev.pos.discount_service.service.strategy.value.DiscountValueStrategy;

import java.math.BigDecimal;

@Component
public class DiscountStrategyResolver {

    public DiscountScopeStrategy resolve(DiscountScope scope, DiscountType type, BigDecimal value) {
        DiscountValueStrategy valueStrategy = switch (type) {
            case PERCENTAGE -> new PercentageDiscountStrategy(value);
            case FIXED -> new FixedValueDiscountStrategy(value);
        };

        return switch (scope) {
            case ORDER -> new OrderLevelDiscountStrategy(valueStrategy);
            case LINE_ITEM -> new LineItemDiscountStrategy(valueStrategy);
        };
    }
}
