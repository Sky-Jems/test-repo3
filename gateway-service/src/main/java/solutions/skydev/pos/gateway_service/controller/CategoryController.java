package solutions.skydev.pos.gateway_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<String> createCategory(@RequestBody String category) {
        try {
            String response = this.categoryProducer.sendCategoryCreateCommand(category);
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating category: " + e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateCategory(@PathVariable String id, @RequestBody String category) {
        try {
            String response = this.categoryProducer.sendCategoryUpdateCommand(id ,category);
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating category: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable String id) {
        try {
            String response = this.categoryProducer.sendCategoryDeleteCommand(id);
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting category: " + e.getMessage());
        }
    }
    
    @PostMapping("/{id}/products")
    public ResponseEntity<String> createCategoryProduct(@RequestBody String requestBody) {
        this.categoryProducer.sendCategoryProductCreateCommand(requestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
