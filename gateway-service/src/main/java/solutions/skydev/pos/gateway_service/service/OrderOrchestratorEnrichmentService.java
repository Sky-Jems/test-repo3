package solutions.skydev.pos.gateway_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import solutions.skydev.pos.common.order_orchestrator_service.dto.response.OrderTransactionResponseDto;
import solutions.skydev.pos.common.order_service.dto.response.OrderResponseDto;

import java.time.OffsetDateTime;

@Service
public class OrderOrchestratorEnrichmentService {
    private final OrderServiceClient orderServiceClient;
    private final ProductEnrichmentService productEnrichmentService;
    
    @Autowired
    public OrderOrchestratorEnrichmentService(OrderServiceClient orderServiceClient, ProductEnrichmentService productEnrichmentService) {
        this.orderServiceClient = orderServiceClient;
        this.productEnrichmentService = productEnrichmentService;
    }
    
    
    public Mono<OrderTransactionResponseDto> enrichWithOrderAndProduct (Mono<OrderTransactionResponseDto> orderTransactionResponseDtoMono) {
        return orderTransactionResponseDtoMono.flatMap(orderTransactionResponseDto -> {
            Mono<OrderResponseDto> orderResponseMono = this.orderServiceClient.fetchOrderById(String.valueOf(orderTransactionResponseDto.getOrderId()));
            return Mono.zip(
                    Mono.just(orderTransactionResponseDto),
                    productEnrichmentService.enrichOrderLineItems(orderResponseMono),
                    (transactionResponse, orderResponse) -> {
                        transactionResponse.setOrder(orderResponse);
                        return transactionResponse;
                    }
            ).onErrorResume(e -> Mono.error(new RuntimeException("Error fetching order: " + e.getMessage())));
        }); 
    }

    public Flux<OrderTransactionResponseDto> enrichTransactionsWithDateFilter(Flux<OrderTransactionResponseDto> orderTransactionResponseDtoFlux, OffsetDateTime startDate, OffsetDateTime endDate) {
        return orderTransactionResponseDtoFlux
                .flatMap(orderTransactionResponseDto -> {
                    Mono<OrderResponseDto> orderResponseDtoMono = this.orderServiceClient.fetchOrderByIdAndDateBetween(
                                    String.valueOf(orderTransactionResponseDto.getOrderId()), startDate, endDate)
                            .onErrorResume(throwable -> Mono.empty());
                    return Mono.zip(
                            Mono.just(orderTransactionResponseDto),
                            productEnrichmentService.enrichOrderLineItems(orderResponseDtoMono),
                            (transactionResponse, orderResponse) -> {
                                transactionResponse.setOrder(orderResponse);
                                return transactionResponse;
                            });
                });
    }
}
