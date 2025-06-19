package solutions.skydev.pos.product_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.product_service.model.entity.VariantOptionValue;
import solutions.skydev.pos.product_service.repository.VariantOptionValueRepository;

import java.util.List;

@Service
public class VariantOptionValueImpl implements VariantOptionValueService {
    private final VariantOptionValueRepository variantOptionValueRepository;

    @Autowired
    public VariantOptionValueImpl(VariantOptionValueRepository variantOptionValueRepository) {
        this.variantOptionValueRepository = variantOptionValueRepository;
    }

    public VariantOptionValue addVariantOptionValue(VariantOptionValue variantOptionValue) {
        return variantOptionValueRepository.save(variantOptionValue);
    }

    public VariantOptionValue getVariantOptionValueById(Long id) {
        return variantOptionValueRepository.findById(id).orElse(null);
    }

    public VariantOptionValue updateVariantOptionValue(Long id, VariantOptionValue variantOptionValue) {
        variantOptionValue.setId(id);
        return variantOptionValueRepository.save(variantOptionValue);
    }

    public void deleteVariantOptionValue(Long id) {
        variantOptionValueRepository.deleteById(id);
    }
    
    public List<VariantOptionValue> findAll() {
        return variantOptionValueRepository.findAll();
    }

    public List<VariantOptionValue> getVariantOptionValuesByVariantOptionId(Long id) {
        return variantOptionValueRepository.findVariantOptionValuesByVariantOptionId(id);
    }
    
    public VariantOptionValue findById(Long id) {
        return variantOptionValueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("VariantOptionValue not found with id: " + id));
    }
}
