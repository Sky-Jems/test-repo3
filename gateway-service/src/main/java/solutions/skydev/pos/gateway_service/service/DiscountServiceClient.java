package solutions.skydev.pos.gateway_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import solutions.skydev.pos.common.discount_service.dto.response.DiscountOrderUpdatedResponseDto;

@Service
public class DiscountServiceClient {

    private final WebClient webClient;

    public DiscountServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8086").build();
    }

    public Mono<DiscountOrderUpdatedResponseDto> getDiscountOrderByOrderId(Long orderId) {
        return webClient.get()
                .uri("/discount-order/{orderId}", orderId)
                .retrieve()
                .bodyToMono(DiscountOrderUpdatedResponseDto.class);
    }
}
