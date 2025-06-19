package solutions.skydev.pos.product_service.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solutions.skydev.pos.product_service.model.entity.*;
import solutions.skydev.pos.product_service.repository.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VariantOptionServiceImpl implements VariantOptionService {
    private final VariantOptionRepository variantOptionRepository;
    private final ProductRepository productRepository;
    private final ProductVariantAssignmentRepository productVariantAssignmentRepository;
    private final VariantRepository variantRepository;
    private final VariantOptionValueRepository variantOptionValueRepository;

    @Autowired
    public VariantOptionServiceImpl(VariantOptionRepository variantOptionRepository,
                                    ProductRepository productRepository,
                                    ProductVariantAssignmentRepository productVariantAssignmentRepository,
                                    VariantRepository variantRepository,
                                    VariantOptionValueRepository variantOptionValueRepository) {
        this.variantOptionRepository = variantOptionRepository;
        this.productRepository = productRepository;
        this.productVariantAssignmentRepository = productVariantAssignmentRepository;
        this.variantRepository = variantRepository;
        this.variantOptionValueRepository = variantOptionValueRepository;
    }
    public VariantOption addVariantOption(Product product, VariantOption variantOption) {
        variantOption.setProduct(product);
        return variantOptionRepository.save(variantOption);
    }

    @Transactional
    public List<VariantOption> createVariantOptions(List<VariantOption> variantOptions) {
        List<VariantOption> variants = variantOptionRepository.saveAll(variantOptions);
        variants.forEach(variantOption -> {
            variantOption.getValues().forEach(variantOptionValue -> {
                variantOptionValue.setVariantOption(variantOption);
            });
        });
        return variantOptionRepository.saveAll(variants);
    }

    public VariantOption getVariantOption(Long id) {
        return variantOptionRepository.findById(id).orElse(null);
    }

    public VariantOption updateVariantOption(Long id, VariantOption variantOption) {
        variantOption.getValues().forEach(value -> {
            value.setVariantOption(variantOption);
        });
        return variantOptionRepository.save(variantOption);
    }

    public void deleteVariantOption(Long id) {
        variantOptionRepository.deleteById(id);
    }

    public List<VariantOption> findAll() {
        return variantOptionRepository.findAll();
    }

    public List<VariantOption> getVariantOptionsByProductId(Long productId) {
        return variantOptionRepository.findAllByProductId(productId);
    }

    @Transactional
    public void generateProductVariantCombinations(List<VariantOption> variantOptions) {
        // Assume passed product is the same for every variant option. TODO: Handle non existent product
        Product product = productRepository.findById(variantOptions.getFirst().getProduct().getId()).orElse(null);
        List<Set<Long>> variantOptionValueIds = Lists.newArrayList();
        variantOptions.forEach(option -> {
            Set<Long> valueIds = option.getValues().stream().map(VariantOptionValue::getId).collect(Collectors.toSet());
            variantOptionValueIds.add(valueIds);
        });

        Set<List<Long>> setVariationIds = Sets.cartesianProduct(variantOptionValueIds);
        setVariationIds.forEach(variationIds -> {
            Variant variant = new Variant();
            variant.setProduct(product);
            Variant savedVariant = variantRepository.save(variant);
            List<ProductVariantAssignment> productVariantAssignments = Lists.newArrayList();
            variantOptionValueRepository.findAllById(variationIds).forEach(optionValue -> {
                ProductVariantAssignment productVariantAssignment = new ProductVariantAssignment();
                productVariantAssignment.setVariant(savedVariant);
                productVariantAssignment.setVariantOptionValue(optionValue);
                productVariantAssignments.add(productVariantAssignment);
            });
            productVariantAssignmentRepository.saveAll(productVariantAssignments);
        });
    }
}
