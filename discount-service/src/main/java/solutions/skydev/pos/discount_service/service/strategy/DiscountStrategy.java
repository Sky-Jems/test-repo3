package solutions.skydev.pos.discount_service.service.strategy;

import solutions.skydev.pos.discount_service.model.entity.Discount;
import solutions.skydev.pos.discount_service.model.entity.DiscountVariant;
import solutions.skydev.pos.discount_service.model.entity.Order;
import solutions.skydev.pos.discount_service.model.enums.DiscountType;

import java.util.List;

public interface DiscountStrategy {
    // Raw implementation of the discount strategy interface, will be improved later
    DiscountType getType(); // e.g. "percentage", "fixed_value"
    double calculateDiscount(Order order, Discount discount, List<DiscountVariant> variants);

}
