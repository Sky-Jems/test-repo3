package solutions.skydev.pos.discount_service.service;

import solutions.skydev.pos.discount_service.model.entity.Discount;
import solutions.skydev.pos.discount_service.model.entity.DiscountLineItem;
import solutions.skydev.pos.discount_service.model.entity.DiscountProduct;
import solutions.skydev.pos.discount_service.model.entity.Order;

import java.util.List;
import java.util.Optional;

public interface DiscountService {
    Discount createDiscount(Discount discount);
    Discount findById(Long id);
    Optional<Discount> findByName(String name);
    Discount updateDiscount(Discount discount);
    Discount deleteDiscount(Long id);
    List<DiscountProduct> getProductsByDiscountId(Long id);
    List<Discount> findAll();
}
