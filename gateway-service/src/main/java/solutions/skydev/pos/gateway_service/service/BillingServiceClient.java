package solutions.skydev.pos.gateway_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import solutions.skydev.pos.common.billing_service.dto.response.BillingRequestResponseDto;

@Service
public class BillingServiceClient {
    
    private final WebClient webClient;
    
    public BillingServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8087").build();
    }
    
    public Flux<BillingRequestResponseDto> getBillingRequestsByOrderId(Long orderId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/billing-requests")
                        .queryParam("order_id", orderId)
                        .build())
                .retrieve()
                .bodyToFlux(BillingRequestResponseDto.class);
    }
}
