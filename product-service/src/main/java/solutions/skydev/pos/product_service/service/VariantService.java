package solutions.skydev.pos.product_service.service;

import solutions.skydev.pos.product_service.model.entity.Variant;

import java.util.List;

public interface VariantService {
    Variant addVariant(Variant variant);
    Variant getVariantById(Long id);
    Variant updateVariant(Long variantId, Variant variant);
    void deleteVariant(Long variantId);
    List<Variant> getVariantsByProductId(Long productId);
    List<Variant> getVariantsByProductIdAndOptionId(Long id, Long variantOptionId);
    List<Variant> findByProductIdAndVariantOptionValueIds(Long productId, List<Long> variantOptionValueIds);
}
