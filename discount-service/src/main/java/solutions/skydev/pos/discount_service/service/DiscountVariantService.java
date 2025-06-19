package solutions.skydev.pos.discount_service.service;

import solutions.skydev.pos.discount_service.model.entity.DiscountVariant;

import java.util.List;

public interface DiscountVariantService {
    DiscountVariant save(DiscountVariant discountVariant);
    DiscountVariant deleteById(DiscountVariant discount);
    List<DiscountVariant> findAllByDiscountId(Long discountId);
}
