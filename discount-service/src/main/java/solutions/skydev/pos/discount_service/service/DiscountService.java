package solutions.skydev.pos.discount_service.service;

import solutions.skydev.pos.discount_service.model.entity.Discount;
import solutions.skydev.pos.discount_service.model.entity.DiscountLineItem;
import solutions.skydev.pos.discount_service.model.entity.DiscountVariant;
import solutions.skydev.pos.discount_service.model.entity.Order;

import java.util.List;

public interface DiscountService {
    DiscountLineItem applyDiscount(Order order, Discount discount);
    Discount createDiscount(Discount discount);
    Discount findById(Long id);
    Discount updateDiscount(Discount discount);
    Discount deleteDiscount(Long id);
    List<DiscountVariant> getVariantsByDiscountId(Long id);
    List<Discount> findAll();
}
