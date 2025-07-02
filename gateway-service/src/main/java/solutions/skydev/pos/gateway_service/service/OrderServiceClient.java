package solutions.skydev.pos.gateway_service.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import solutions.skydev.pos.common.order_service.dto.response.OrderResponseDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Optional;

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

    public Mono<OrderResponseDto> fetchOrderByIdAndDateBetween(String orderId, OffsetDateTime startDate, OffsetDateTime endDate) {
        String from = URLEncoder.encode(startDate.toString(), StandardCharsets.UTF_8);
        String to = URLEncoder.encode(endDate.toString(), StandardCharsets.UTF_8);
        return orderWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/orders/{id}")
                        .queryParam("start_date", from)
                        .queryParam("end_date", to)
                        .build(orderId))
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .bodyToMono(OrderResponseDto.class);
    }
}
