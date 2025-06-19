package solutions.skydev.pos.product_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import solutions.skydev.pos.product_service.model.dto.response.ProductVariantAssignmentResponseDto;
import solutions.skydev.pos.product_service.model.mapper.ProductVariantAssignmentMapper;
import solutions.skydev.pos.product_service.service.ProductVariantAssignmentService;

import java.util.List;

@RestController
@RequestMapping("/product-variant-assignments")
public class ProductVariantAssignmentController {

    private final ProductVariantAssignmentService productVariantAssignmentService;
    private final ProductVariantAssignmentMapper productVariantAssignmentMapper;

    @Autowired
    public ProductVariantAssignmentController(ProductVariantAssignmentService productVariantAssignmentService, ProductVariantAssignmentMapper productVariantAssignmentMapper) {
        this.productVariantAssignmentService = productVariantAssignmentService;
        this.productVariantAssignmentMapper = productVariantAssignmentMapper;
    }

    @GetMapping
    public List<ProductVariantAssignmentResponseDto> getProductVariantAssignment(@RequestParam(value = "variant_id", required = false) Long variantId) {
        // TODO: this should havppen in the service layer
        if (variantId == null) {
            return productVariantAssignmentMapper.toResponseDtoList(
                    productVariantAssignmentService.getAllProductVariantAssignments()
            );
        }
        return productVariantAssignmentMapper.toResponseDtoList(
                productVariantAssignmentService.getProductVariantAssignmentsByVariantId(variantId)
        );
    }
}
