package solutions.skydev.pos.gateway_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import solutions.skydev.pos.common.product_service.dto.response.CategoryResponseDto;
import solutions.skydev.pos.common.product_service.dto.response.ProductResponseDto;

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
    
    public Mono<CategoryResponseDto> getCategoryById(Long id) {
        return webClient.get()
                .uri("/categories/{id}", id)
                .retrieve()
                .bodyToMono(CategoryResponseDto.class);
    }

}
