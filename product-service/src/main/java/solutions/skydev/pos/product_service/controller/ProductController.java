package solutions.skydev.pos.product_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solutions.skydev.pos.product_service.model.dto.response.ProductResponseDto;
import solutions.skydev.pos.product_service.model.dto.response.VariantOptionResponseDto;
import solutions.skydev.pos.product_service.model.dto.response.VariantResponseDto;
import solutions.skydev.pos.product_service.model.entity.Product;
import solutions.skydev.pos.product_service.model.entity.Variant;
import solutions.skydev.pos.product_service.model.entity.VariantOption;
import solutions.skydev.pos.product_service.model.mapper.ProductMapper;
import solutions.skydev.pos.product_service.model.mapper.VariantMapper;
import solutions.skydev.pos.product_service.model.mapper.VariantOptionMapper;
import solutions.skydev.pos.product_service.service.ProductService;
import solutions.skydev.pos.product_service.service.VariantOptionService;
import solutions.skydev.pos.product_service.service.VariantService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final VariantOptionService variantOptionService;
    private final VariantService variantService;
    private final ProductMapper productMapper;
    private final VariantOptionMapper variantOptionMapper;
    private final VariantMapper variantMapper;

    @Autowired
    public ProductController(ProductService productService, VariantOptionService variantOptionService,
                             VariantService variantService, ProductMapper productMapper,
                             VariantOptionMapper variantOptionMapper, VariantMapper variantMapper) {
        this.productService = productService;
        this.variantOptionService = variantOptionService;
        this.variantService = variantService;
        this.productMapper = productMapper;
        this.variantOptionMapper = variantOptionMapper;
        this.variantMapper = variantMapper;
    }

    @GetMapping("/{id}")
    public ProductResponseDto getProductById(@PathVariable Long id) {
        Product product = productService.findById(id);
        return productMapper.toResponseDto(product);
    }

    @GetMapping
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productService.findAll();
        return productMapper.toResponseDtoList(products);
    }

    @GetMapping("/{id}/variant-options")
    public List<VariantOptionResponseDto> getVariantsByProductId(@PathVariable Long id) {
        List<VariantOption> variantOptions = variantOptionService.getVariantOptionsByProductId(id);
        return variantOptionMapper.toResponseDtoList(variantOptions);
    }

    @GetMapping("/{id}/variants")
    public List<VariantResponseDto> getAllVariantsByProductId(
            @PathVariable Long id,
            @RequestParam(required = false) Optional<Long> variantOptionId,
            @RequestParam(required = false) Optional<List<Long>> valueIds) {
        List<Variant> variants;
        if (variantOptionId.isPresent()) {
            variants = variantService.getVariantsByProductIdAndOptionId(id, variantOptionId.get());
        } else if (valueIds.isPresent() && !valueIds.get().isEmpty()) {
            List<Long> valueIdList = valueIds.get();
            variants = variantService.findByProductIdAndVariantOptionValueIds(id, valueIdList);
        } else {
            variants = variantService.getVariantsByProductId(id);
        }
        return this.variantMapper.toResponseDtoList(variants);
    }
}
