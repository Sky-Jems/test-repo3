package solutions.skydev.pos.product_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solutions.skydev.pos.product_service.model.dto.response.VariantOptionResponseDto;
import solutions.skydev.pos.product_service.model.dto.response.VariantOptionValueResponseDto;
import solutions.skydev.pos.product_service.model.entity.VariantOption;
import solutions.skydev.pos.product_service.model.entity.VariantOptionValue;
import solutions.skydev.pos.product_service.model.mapper.VariantOptionMapper;
import solutions.skydev.pos.product_service.model.mapper.VariantOptionValueMapper;
import solutions.skydev.pos.product_service.service.VariantOptionService;
import solutions.skydev.pos.product_service.service.VariantOptionValueService;

import java.util.List;

@RestController
@RequestMapping("/variant-options")
public class VariantOptionController {
    
    private final VariantOptionService variantOptionService;
    private final VariantOptionMapper variantOptionMapper;
    private final VariantOptionValueService variantOptionValueService;
    private final VariantOptionValueMapper variantValuesValuesMapper;
    
    @Autowired
    public VariantOptionController(VariantOptionService variantOptionService, VariantOptionMapper variantOptionMapper, VariantOptionValueService variantOptionValueService, VariantOptionValueMapper variantValuesValuesMapper) {
        this.variantOptionService = variantOptionService;
        this.variantOptionMapper = variantOptionMapper;
        this.variantOptionValueService = variantOptionValueService;
        this.variantValuesValuesMapper = variantValuesValuesMapper;
    }
    
    @GetMapping
    public List<VariantOptionResponseDto> getAllVariantOptions() {
        List<VariantOption> variantOptions = variantOptionService.findAll();
        return variantOptionMapper.toResponseDtoList(variantOptions);
    }
    
    @GetMapping("/{id}")
    public VariantOptionResponseDto getVariantOptionById(@PathVariable Long id) {
        VariantOption variantOption = variantOptionService.getVariantOption(id);
        return variantOptionMapper.toResponseDto(variantOption);
    }

    @GetMapping("/{id}/variant-option-values")
    public List<VariantOptionValueResponseDto> getVariantOptionValuesByVariantOptions(@PathVariable("id") Long id) {
        List<VariantOptionValue> variantOptionValues = variantOptionValueService.getVariantOptionValuesByVariantOptionId(id);
        return variantValuesValuesMapper.toDtoList(variantOptionValues);
    }
}
