package solutions.skydev.pos.product_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solutions.skydev.pos.product_service.model.dto.response.VariantOptionValueResponseDto;
import solutions.skydev.pos.product_service.model.entity.VariantOptionValue;
import solutions.skydev.pos.product_service.model.mapper.VariantOptionValueMapper;
import solutions.skydev.pos.product_service.service.VariantOptionValueService;

import java.util.List;

@RestController
@RequestMapping("/variant-option-values")
public class VariantOptionValueController {
    private final VariantOptionValueService variantOptionValueService;
    private final VariantOptionValueMapper variantOptionValueMapper;
    
    @Autowired
    public VariantOptionValueController(VariantOptionValueService variantOptionValueService, VariantOptionValueMapper variantOptionValueMapper) {
        this.variantOptionValueService = variantOptionValueService;
        this.variantOptionValueMapper = variantOptionValueMapper;
    }
    
    @GetMapping
    public List<VariantOptionValueResponseDto> getAllVariantOptionValues() {
        List<VariantOptionValue> variantOptionValues = variantOptionValueService.findAll();
        return variantOptionValueMapper.toDtoList(variantOptionValues);
    }
    
    @GetMapping("/{id}")
    public VariantOptionValueResponseDto getVariantOptionValueById(@PathVariable Long id) {
        VariantOptionValue variantOptionValue = variantOptionValueService.findById(id);
        return variantOptionValueMapper.toDto(variantOptionValue);
    }
        
}
