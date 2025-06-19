package solutions.skydev.pos.gateway_service.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import solutions.skydev.pos.gateway_service.model.dto.response.*;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceClient {

    private final WebClient webClient;

    public ProductServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    public Mono<ProductResponseDto> getById(Long id) {
        return webClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                .bodyToMono(ProductResponseDto.class);
    }

    public Mono<VariantResponseDto> getVariantById(Long id) {
        return webClient.get()
                .uri("/variants/{id}", id)
                .retrieve()
                .bodyToMono(VariantResponseDto.class);
    }

    public Flux<VariantResponseDto> getVariantsByProductId(Long id, Optional<Long> variantOptionId, Optional<String> value) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/products/{id}/variants")
                        .queryParam("variantOptionId", variantOptionId)
                        .queryParam("value", value)
                        .build(id))
                .retrieve()
                .bodyToFlux(VariantResponseDto.class);
    }

    public Mono<VariantOptionResponseDto> getVariantOptionById(Long id) {
        return webClient.get()
                .uri("/variant-options/{id}", id)
                .retrieve()
                .bodyToMono(VariantOptionResponseDto.class);
    }

    public Mono<VariantOptionValueResponseDto> getVariantOptionValue(Long id) {
        return webClient.get()
                .uri("/variant-option-values/{id}", id)
                .retrieve()
                .bodyToMono(VariantOptionValueResponseDto.class);
    }

    public Flux<ProductVariantAssignmentResponseDto> getVariantAssignmentsByVariantId(Long variantId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/product-variant-assignments")
                        .queryParam("variant_id", variantId)
                        .build())
                .retrieve()
                .bodyToFlux(ProductVariantAssignmentResponseDto.class);
    }
}
