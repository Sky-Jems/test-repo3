package solutions.skydev.pos.discount_service.service;

import solutions.skydev.pos.discount_service.model.entity.DiscountLineItem;

public interface DiscountLineItemService {
    DiscountLineItem save(DiscountLineItem discountLineItem);
    DiscountLineItem findById(Long id);
}
