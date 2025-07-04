package solutions.skydev.pos.gateway_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import solutions.skydev.pos.common.billing_service.dto.response.BillingRequestResponseDto;
import solutions.skydev.pos.common.order_orchestrator_service.dto.response.OrderTransactionResponseDto;
import solutions.skydev.pos.common.order_service.dto.response.OrderResponseDto;
import solutions.skydev.pos.common.payment_service.dto.response.PaymentResponseDto;

import java.util.List;

import java.time.OffsetDateTime;

@Service
public class OrderOrchestratorEnrichmentService {
    private final OrderServiceClient orderServiceClient;
    private final ProductEnrichmentService productEnrichmentService;
    private final BillingServiceClient billingServiceClient;
    private final PaymentServiceClient paymentServiceClient;

    @Autowired
    public OrderOrchestratorEnrichmentService(OrderServiceClient orderServiceClient, ProductEnrichmentService productEnrichmentService, BillingServiceClient billingServiceClient, PaymentServiceClient paymentServiceClient) {
        this.orderServiceClient = orderServiceClient;
        this.productEnrichmentService = productEnrichmentService;
        this.billingServiceClient = billingServiceClient;
        this.paymentServiceClient = paymentServiceClient;
    }


    public Mono<OrderTransactionResponseDto> enrichWithOrderAndProduct(Mono<OrderTransactionResponseDto> orderTransactionResponseDtoMono) {
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

    public Mono<OrderTransactionResponseDto> enrichWithBillingAndPayment(Mono<OrderTransactionResponseDto> orderTransactionResponseDtoMono) {
        return orderTransactionResponseDtoMono.flatMap(orderTransactionResponseDto -> {
            Long orderId = orderTransactionResponseDto.getOrderId();

            // Get billing requests for the order
            return billingServiceClient.getBillingRequestsByOrderId(orderId)
                    .collectList()
                    .flatMap(billingRequests -> {
                        if (billingRequests.isEmpty()) {
                            return Mono.just(orderTransactionResponseDto);
                        }

                        // Use the first billing request for the order
                        BillingRequestResponseDto billingRequest = billingRequests.get(0);
                        orderTransactionResponseDto.setBilling(billingRequest);

                        // Get payments for the billing request
                        return paymentServiceClient.getPaymentsByBillingRequestId(billingRequest.getId())
                                .collectList()
                                .map(payments -> {
                                    orderTransactionResponseDto.setPayments(payments);
                                    return orderTransactionResponseDto;
                                });
                    })
                    .onErrorResume(e -> {
                        // Log the error but don't fail the entire operation
                        System.err.println("Error enriching with billing and payment: " + e.getMessage());
                        return Mono.just(orderTransactionResponseDto);
                    });
        });
    }

    public Mono<OrderTransactionResponseDto> enrichWithOrderProductBillingAndPayment(Mono<OrderTransactionResponseDto> orderTransactionResponseDtoMono) {
        return enrichWithOrderAndProduct(orderTransactionResponseDtoMono)
                .flatMap(orderTransactionResponseDto -> enrichWithBillingAndPayment(Mono.just(orderTransactionResponseDto)));
    }
}
