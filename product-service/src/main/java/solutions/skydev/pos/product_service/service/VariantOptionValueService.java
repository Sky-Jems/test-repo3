package solutions.skydev.pos.product_service.service;

import solutions.skydev.pos.product_service.model.entity.VariantOptionValue;

import java.util.List;

public interface VariantOptionValueService {
    VariantOptionValue addVariantOptionValue(VariantOptionValue variantOptionValue);
    VariantOptionValue updateVariantOptionValue(Long variantOptionValueId, VariantOptionValue variantOptionValue);
    void deleteVariantOptionValue(Long variantOptionValueId);
    VariantOptionValue getVariantOptionValueById(Long variantOptionValueId);
    List<VariantOptionValue> findAll();
    List<VariantOptionValue> getVariantOptionValuesByVariantOptionId(Long productId);
    VariantOptionValue findById(Long id);

//
//    List<VariantOptionValue> getAllVariantOptionValuesByVariantOptionId(Long variantOptionId);
}
