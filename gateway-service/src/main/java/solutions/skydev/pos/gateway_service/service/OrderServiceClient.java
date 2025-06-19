package solutions.skydev.pos.gateway_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import solutions.skydev.pos.gateway_service.model.dto.response.OrderResponseDto;

@Service
public class OrderServiceClient {
    
    private final WebClient orderWebClient;
    
    public OrderServiceClient(WebClient.Builder webClientBuilder) {
        this.orderWebClient = webClientBuilder.baseUrl("http://localhost:8085").build();
    }

    public Mono<OrderResponseDto> fetchOrderById(String orderId) {
        return orderWebClient.get()
                .uri("/orders/{id}", orderId)
                .retrieve()
                .bodyToMono(OrderResponseDto.class);
    }
}
