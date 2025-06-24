package solutions.skydev.pos.gateway_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import solutions.skydev.pos.common.product_service.dto.request.ProductRequestDto;
import solutions.skydev.pos.common.product_service.dto.response.ProductResponseDto;
import solutions.skydev.pos.gateway_service.producer.ProductProducer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductProducer productProducer;

    @Autowired
    public ProductController(ProductProducer productProducer) {
        this.productProducer = productProducer;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto product) {
        try {
            ProductResponseDto response = productProducer.sendProductCreateCommand(product);
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponseDto> updateProduct(@RequestBody ProductRequestDto product) {
        try {
            ProductResponseDto response = productProducer.sendProductUpdateCommand(product);
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponseDto> deleteProduct(@PathVariable String id) {
        try {
            ProductResponseDto response = productProducer.sendProductDeleteCommand(id);
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
