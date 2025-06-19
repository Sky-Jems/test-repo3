package solutions.skydev.pos.product_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solutions.skydev.pos.product_service.model.entity.Product;
import solutions.skydev.pos.product_service.model.entity.Variant;
import solutions.skydev.pos.product_service.repository.ProductRepository;
import solutions.skydev.pos.product_service.repository.VariantRepository;

import java.util.List;

@Service
public class VariantServiceImpl implements VariantService {
    private final VariantRepository variantRepository;
    private final ProductRepository productRepository;

    @Autowired
    public VariantServiceImpl(VariantRepository variantRepository, ProductRepository productRepository) {
        this.variantRepository = variantRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Variant addVariant(Variant variant) {
        Product product = productRepository.findById(variant.getProduct().getId()).orElseThrow(EntityNotFoundException::new);
        variant.setProduct(product);
        return variantRepository.save(variant);
    }

    public Variant getVariantById(Long id) {
        return variantRepository.findById(id).orElse(null);
    }

    @Transactional
    public Variant updateVariant(Long variantId, Variant variant) {
        variantRepository.findById(variantId).orElseThrow(EntityNotFoundException::new);
        Product product = productRepository.findById(variant.getProduct().getId()).orElseThrow(EntityNotFoundException::new);
        variant.setProduct(product);
        return variantRepository.save(variant);
    }

    public void deleteVariant(Long id) {
        variantRepository.deleteById(id);
    }

    public List<Variant> getVariantsByProductId(Long productId) {
        return variantRepository.findByProductId(productId);
    }

    public List<Variant> getVariantsByProductIdAndOptionId(Long id, Long variantOptionId) {
        return variantRepository.findByProductIdAndVariantOptionId(id, variantOptionId);
    }

    public List<Variant> findByProductIdAndVariantOptionValueIds(Long productId, List<Long> variantOptionValueIds) {
        return variantRepository.findByProductIdAndVariantOptionValueIds(productId, variantOptionValueIds);
    }
}
