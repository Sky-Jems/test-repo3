package solutions.skydev.pos.gateway_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import solutions.skydev.pos.gateway_service.producer.VariantOptionValueProducer;

@Controller
@RequestMapping("/variant-option-values")
public class VariantOptionValueController {

    private final VariantOptionValueProducer variantOptionValueProducer;

    @Autowired
    public VariantOptionValueController(VariantOptionValueProducer variantOptionValueProducer) {
        this.variantOptionValueProducer = variantOptionValueProducer;
    }

    @PostMapping
    public ResponseEntity<String> createVariantOptionValue(@RequestBody String variantOptionValue) {
        this.variantOptionValueProducer.sendVariantOptionValueCreateCommand(variantOptionValue);
        return ResponseEntity.ok("ok");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateVariantOptionValue(@PathVariable String id, @RequestBody String variantOptionValue) {
        this.variantOptionValueProducer.sendVariantOptionValueUpdateCommand(id, variantOptionValue);
        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVariantOptionValue(@PathVariable String id) {
        this.variantOptionValueProducer.sendVariantOptionValueDeleteCommand(id);
        return ResponseEntity.ok("ok");
    }
}
