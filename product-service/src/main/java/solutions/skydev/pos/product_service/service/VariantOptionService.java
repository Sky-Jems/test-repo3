package solutions.skydev.pos.product_service.service;

import solutions.skydev.pos.product_service.model.entity.VariantOption;
import solutions.skydev.pos.product_service.model.entity.Product;

import java.util.List;

public interface VariantOptionService {
    VariantOption addVariantOption(Product product, VariantOption variantOption);
    List<VariantOption> createVariantOptions(List<VariantOption> variantOptions);
    VariantOption getVariantOption(Long id);
    VariantOption updateVariantOption(Long productId, VariantOption variantOption);
    void deleteVariantOption(Long id);
    List<VariantOption> findAll();
    List<VariantOption> getVariantOptionsByProductId(Long productId);

    void generateProductVariantCombinations(List<VariantOption> variantOptions);
}
