//package solutions.skydev.pos.gateway_service.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Flux;
//import solutions.skydev.pos.gateway_service.model.dto.response.VariantResponseDto;
//import solutions.skydev.pos.gateway_service.service.ProductEnrichmentService;
//import solutions.skydev.pos.gateway_service.service.ProductOrchestratorServiceClient;
//
//import java.util.Comparator;
//import java.util.List;
//import java.util.Optional;
//
//
//@RestController
//@RequestMapping("/product-variants")
//public class ProductOrchestratorController {
//    private final ProductEnrichmentService productEnrichmentService;
//    private final ProductOrchestratorServiceClient productOrchestratorServiceClient;
//
//    @Autowired
//    public ProductOrchestratorController(ProductEnrichmentService productEnrichmentService,
//                                         ProductOrchestratorServiceClient productOrchestratorServiceClient) {
//        this.productEnrichmentService = productEnrichmentService;
//        this.productOrchestratorServiceClient = productOrchestratorServiceClient;
//    }
//
//    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Flux<VariantResponseDto> fetchVariantsByProductId(@PathVariable Long id,
//                                                             @RequestParam(required = false) Optional<Long> variantOptionId,
//                                                             @RequestParam(required = false) Optional<List<Long>> valueIds) {
//        Flux<VariantResponseDto> variantResponseDtoFlux = this.productOrchestratorServiceClient.fetchVariantsByProductId(id, variantOptionId, valueIds);
//        return variantResponseDtoFlux.
//                flatMap(productEnrichmentService::enrichVariantWithCombinations)
//                .sort(Comparator.comparing(VariantResponseDto::getVariantId));
//    }
//}
