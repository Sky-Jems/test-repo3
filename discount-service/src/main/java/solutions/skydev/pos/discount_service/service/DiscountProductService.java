package solutions.skydev.pos.discount_service.service;

import solutions.skydev.pos.discount_service.model.entity.DiscountProduct;

import java.util.List;

public interface DiscountProductService {
    DiscountProduct save(DiscountProduct discountProduct);
    DiscountProduct deleteById(DiscountProduct discount);
    List<DiscountProduct> findAllByDiscountId(Long discountId);
}
