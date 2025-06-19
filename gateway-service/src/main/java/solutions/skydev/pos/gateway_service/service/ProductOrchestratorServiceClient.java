package solutions.skydev.pos.gateway_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import solutions.skydev.pos.gateway_service.model.dto.response.VariantResponseDto;

import java.util.List;
import java.util.Optional;

@Service
public class ProductOrchestratorServiceClient {
    private final WebClient webClient;

    public ProductOrchestratorServiceClient(WebClient.Builder wegClientBuilder) {
        this.webClient = wegClientBuilder.baseUrl("http://localhost:8080").build();
    }

    public Flux<VariantResponseDto> fetchVariantsByProductId(Long productId, Optional<Long> variantOptionId, Optional<List<Long>> valueIds) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/products/{id}/variants")
                        .queryParamIfPresent("variantOptionId", variantOptionId)
                        .queryParamIfPresent("valueIds", valueIds)
                        .build(productId))
                .retrieve()
                .bodyToFlux(VariantResponseDto.class);
    }
}
