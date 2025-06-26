package solutions.skydev.pos.gateway_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import solutions.skydev.pos.common.order_orchestrator_service.dto.response.OrderTransactionResponseDto;

@Service
public class OrderOrchestratorServiceClient {

    private final WebClient webClient;

    public OrderOrchestratorServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8083").build();
    }

    public Mono<OrderTransactionResponseDto> fetchOrderTransactionById(Long id) {
        return webClient.get()
                .uri("/order-transactions/{id}", id)
                .retrieve()
                .bodyToMono(OrderTransactionResponseDto.class);
    }

    public Mono<OrderTransactionResponseDto> fetchOrderTransactionByOrderId(Long orderId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/order-transactions")
                        .queryParam("order_id", orderId)
                        .build())
                .retrieve()
                .bodyToFlux(OrderTransactionResponseDto.class)
                .next(); // Get only the first element from the array
    }
}
