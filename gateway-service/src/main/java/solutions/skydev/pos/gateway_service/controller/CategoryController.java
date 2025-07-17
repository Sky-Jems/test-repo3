package solutions.skydev.pos.gateway_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import solutions.skydev.pos.common.error.domain.DomainException;
import solutions.skydev.pos.common.product_service.dto.request.CategoryRequestDto;
import solutions.skydev.pos.common.product_service.dto.response.CategoryResponseDto;
import solutions.skydev.pos.gateway_service.producer.CategoryProducer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryProducer categoryProducer;

    @Autowired
    public CategoryController(CategoryProducer categoryProducer) {
        this.categoryProducer = categoryProducer;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryRequestDto category) throws DomainException, ExecutionException, InterruptedException, TimeoutException {
        CategoryResponseDto response = this.categoryProducer.sendCategoryCreateCommand(category);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable String id, @RequestBody CategoryRequestDto category) throws ExecutionException, InterruptedException, TimeoutException {
        CategoryResponseDto response  = this.categoryProducer.sendCategoryUpdateCommand(id ,category);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CategoryResponseDto> deleteCategory(@PathVariable String id) throws ExecutionException, InterruptedException, TimeoutException {
        CategoryResponseDto response = this.categoryProducer.sendCategoryDeleteCommand(id);
        return ResponseEntity.ok(response);
    }

    // For deletion: check if used
    @PostMapping("/{id}/products")
    public ResponseEntity<String> createCategoryProduct(@RequestBody String requestBody) {
        this.categoryProducer.sendCategoryProductCreateCommand(requestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
