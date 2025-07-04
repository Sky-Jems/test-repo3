package solutions.skydev.pos.gateway_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import solutions.skydev.pos.common.payment_service.dto.response.PaymentResponseDto;

@Service
public class PaymentServiceClient {
    private final WebClient webClient;

    public PaymentServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8084").build();
    }
    
    public Flux<PaymentResponseDto> getPaymentsByBillingRequestId(Long billingRequestId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/payments")
                        .queryParam("billing_request_id", billingRequestId)
                        .build())
                .retrieve()
                .bodyToFlux(PaymentResponseDto.class);
    }
}
