package solutions.skydev.pos.discount_service.service.strategy;

import org.springframework.stereotype.Component;
import solutions.skydev.pos.discount_service.model.entity.Discount;
import solutions.skydev.pos.discount_service.model.entity.DiscountVariant;
import solutions.skydev.pos.discount_service.model.entity.LineItem;
import solutions.skydev.pos.discount_service.model.entity.Order;
import solutions.skydev.pos.discount_service.model.enums.DiscountScope;
import solutions.skydev.pos.discount_service.model.enums.DiscountType;

import java.util.List;

@Component
public class PercentageDiscountStrategy implements DiscountStrategy {

    @Override
    public DiscountType getType() {
        return DiscountType.PERCENTAGE;
    }

    @Override
    public double calculateDiscount(Order order, Discount discount, List<DiscountVariant> variants) {
        if (discount.getScope() == DiscountScope.ORDER) {
            return order.getTotalAmount() * (discount.getValue() / 100);
        }

        List<LineItem> matchedItems = order.getLineItems().stream()
                .filter(item -> matchesVariant(item, variants))
                .toList();

        double discountableAmount = matchedItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        return discountableAmount * (discount.getValue() / 100);
    }

    private boolean matchesVariant(LineItem item, List<DiscountVariant> variantOptions) {
        return variantOptions.stream()
                .anyMatch(opt -> opt.getVariantId().equals(item.getVariantId()));
    }
}


