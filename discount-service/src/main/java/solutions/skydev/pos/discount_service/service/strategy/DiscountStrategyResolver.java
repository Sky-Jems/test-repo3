package solutions.skydev.pos.discount_service.service.strategy;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.discount_service.model.enums.DiscountType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DiscountStrategyResolver {

    private final Map<DiscountType, DiscountStrategy> strategyMap;

    public DiscountStrategyResolver(List<DiscountStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(DiscountStrategy::getType, s -> s));
    }

    public DiscountStrategy resolve(@NotNull DiscountType type) {
        DiscountStrategy strategy = strategyMap.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported discount type: " + type);
        }
        return strategy;
    }
}
