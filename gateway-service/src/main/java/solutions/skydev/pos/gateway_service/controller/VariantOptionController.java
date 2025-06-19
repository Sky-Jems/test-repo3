package solutions.skydev.pos.gateway_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import solutions.skydev.pos.gateway_service.producer.VariantOptionProducer;

@Controller
@RequestMapping("/variant-options")
public class VariantOptionController {
    
    private final VariantOptionProducer variantOptionProducer;
    
    @Autowired
    public VariantOptionController(VariantOptionProducer variantOptionProducer) {
        this.variantOptionProducer = variantOptionProducer;
    }
    
    @PostMapping
    public ResponseEntity<String> createVariantOption(@RequestBody String variantOption) {
        this.variantOptionProducer.sendVariantOptionCreateCommand(variantOption);
        return ResponseEntity.ok("ok");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateVariantOption(@PathVariable String id, @RequestBody String variantOption) {
        this.variantOptionProducer.sendVariantOptionUpdateCommand(id, variantOption);
        return ResponseEntity.ok("ok");
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVariantOption(@PathVariable String id) {
        this.variantOptionProducer.sendVariantOptionDeleteCommand(id);
        return ResponseEntity.ok("ok");
    }
}
