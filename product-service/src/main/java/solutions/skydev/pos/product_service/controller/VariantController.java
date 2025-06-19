package solutions.skydev.pos.product_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solutions.skydev.pos.product_service.model.dto.response.VariantResponseDto;
import solutions.skydev.pos.product_service.model.entity.Variant;
import solutions.skydev.pos.product_service.model.mapper.VariantMapper;
import solutions.skydev.pos.product_service.service.VariantService;

@RestController
@RequestMapping("/variants")
public class VariantController {
    private final VariantService variantService;
    private final VariantMapper variantMapper;

    @Autowired
    public VariantController(VariantService variantService, VariantMapper variantMapper) {
        this.variantService = variantService;
        this.variantMapper = variantMapper;
    }

    @GetMapping("/{id}")
    public VariantResponseDto getVariant(@PathVariable Long id) {
        Variant variant = variantService.getVariantById(id);
        return variantMapper.toResponseDto(variant);
    }
}
