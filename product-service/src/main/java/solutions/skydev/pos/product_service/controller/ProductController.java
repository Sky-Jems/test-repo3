package solutions.skydev.pos.product_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solutions.skydev.pos.common.product_service.dto.response.ProductResponseDto;
import solutions.skydev.pos.product_service.model.entity.Product;
import solutions.skydev.pos.product_service.model.mapper.ProductMapper;
import solutions.skydev.pos.product_service.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
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
}
