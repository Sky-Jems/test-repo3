package solutions.skydev.pos.gateway_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import solutions.skydev.pos.gateway_service.producer.VariantProducer;

@Controller
@RequestMapping("/variants")
public class VariantController {
    private final VariantProducer variantProducer;

    @Autowired
    public VariantController(VariantProducer variantProducer) {
        this.variantProducer = variantProducer;
    }

    @PostMapping
    public ResponseEntity<String> createVariant(@RequestBody String variant) {
        this.variantProducer.sendCreateVariantCommand(variant);
        return ResponseEntity.ok("ok");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateVariant(@PathVariable String id, @RequestBody String variant) {
        this.variantProducer.sendUpdateVariantCommand(id, variant);
        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVariant(@PathVariable String id) {
        this.variantProducer.sendDeleteVariantCommand(id);
        return ResponseEntity.ok("ok");
    }
}
