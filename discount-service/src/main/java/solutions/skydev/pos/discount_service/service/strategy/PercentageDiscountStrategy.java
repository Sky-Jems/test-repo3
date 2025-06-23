package solutions.skydev.pos.discount_service.service.strategy;

import org.springframework.stereotype.Component;
import solutions.skydev.pos.discount_service.model.entity.Discount;
import solutions.skydev.pos.discount_service.model.entity.DiscountProduct;
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
    public double calculateDiscount(Order order, Discount discount, List<DiscountProduct> products) {
        if (discount.getMinSpend() != null && order.getTotalAmount() < discount.getMinSpend()) {
            return 0;
        }

        if (discount.getScope() == DiscountScope.ORDER) {
            return order.getTotalAmount() * (discount.getValue() / 100);
        }

        List<LineItem> matchedItems = order.getLineItems().stream()
                .filter(item -> matchesProduct(item, products))
                .sorted((a, b) -> Double.compare(b.getPrice(), a.getPrice()))
                .toList();

        if (discount.getMinQty() != null) {
            int totalQty = matchedItems.stream()
                    .mapToInt(LineItem::getQuantity)
                    .sum();

            if (totalQty < discount.getMinQty()) {
                return 0;
            }
        }

        int maxQtyCap = discount.getMaxQty() != null ? discount.getMaxQty() : Integer.MAX_VALUE;
        int totalDiscountedQty = 0;
        double discountableAmount = 0;

        for (LineItem item : matchedItems) {
            if (totalDiscountedQty >= maxQtyCap) break;

            int remainingQty = maxQtyCap - totalDiscountedQty;
            int eligibleQty = Math.min(item.getQuantity(), remainingQty);

            discountableAmount += item.getPrice() * eligibleQty;
            totalDiscountedQty += eligibleQty;
        }

        return discountableAmount * (discount.getValue() / 100);
    }

    private boolean matchesProduct(LineItem item, List<DiscountProduct> productOptions) {
        return productOptions.stream()
                .anyMatch(opt -> opt.getProductId().equals(item.getProductId()));
    }
}


