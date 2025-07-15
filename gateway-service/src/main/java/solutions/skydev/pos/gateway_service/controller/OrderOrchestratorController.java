package solutions.skydev.pos.gateway_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import solutions.skydev.pos.common.discount_service.dto.request.DiscountOrderRequestDto;
import solutions.skydev.pos.common.order_orchestrator_service.dto.response.OrderTransactionResponseDto;
import solutions.skydev.pos.common.order_service.dto.request.LineItemRequestDto;
import solutions.skydev.pos.common.order_service.dto.request.OrderRequestDto;
import solutions.skydev.pos.gateway_service.producer.OrderOrchestratorProducer;
import solutions.skydev.pos.gateway_service.service.OrderOrchestratorEnrichmentService;
import solutions.skydev.pos.gateway_service.service.OrderOrchestratorServiceClient;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/order-transaction")
public class OrderOrchestratorController {
    private final OrderOrchestratorProducer orderOrchestratorProducer;

    private final OrderOrchestratorServiceClient orderOrchestratorServiceClient;
    private final OrderOrchestratorEnrichmentService orderOrchestratorEnrichmentService;


    @Autowired
    public OrderOrchestratorController(OrderOrchestratorProducer orderOrchestratorProducer,
                                       OrderOrchestratorServiceClient orderOrchestratorServiceClient,
                                       OrderOrchestratorEnrichmentService orderOrchestratorEnrichmentService
                                       ) {
        this.orderOrchestratorProducer = orderOrchestratorProducer;
        this.orderOrchestratorServiceClient = orderOrchestratorServiceClient;
        this.orderOrchestratorEnrichmentService = orderOrchestratorEnrichmentService;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderTransactionResponseDto> getOrderTransactionById(@PathVariable String id) {

        Mono<OrderTransactionResponseDto> orderTransactionResponseDtoMono = this.orderOrchestratorServiceClient.fetchOrderTransactionById(Long.valueOf(id));
        return orderOrchestratorEnrichmentService.enrichWithOrderProductBillingAndPaymentAndDiscount(orderTransactionResponseDtoMono);
    }

    /**
     * Fetches an order transaction by order ID.
     * This endpoint uses a request parameter to specify the order ID.
     *
     * @param orderId The ID of the order to fetch the transaction for
     * @return The enriched order transaction response
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderTransactionResponseDto> getOrderTransactionByOrderId(@RequestParam("order_id") String orderId) {
        Mono<OrderTransactionResponseDto> orderTransactionResponseDtoMono = this.orderOrchestratorServiceClient.fetchOrderTransactionByOrderId(Long.valueOf(orderId));
        return orderOrchestratorEnrichmentService.enrichWithOrderProductBillingAndPaymentAndDiscount(orderTransactionResponseDtoMono);
    }

    @GetMapping(path= "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<OrderTransactionResponseDto> getOrderTransactions(
            @RequestParam("order_status") String orderStatus,
            @RequestParam("start_date") OffsetDateTime startDate,
            @RequestParam("end_date") OffsetDateTime endDate) {
        Flux<OrderTransactionResponseDto> orderTransactionResponseDtoFlux = this.orderOrchestratorServiceClient.fetchOrderTransactionsByOrderStatus(orderStatus);
        Flux<OrderTransactionResponseDto> filteredFlux = orderOrchestratorEnrichmentService.enrichTransactionsWithDateFilter(orderTransactionResponseDtoFlux, startDate, endDate);
        return filteredFlux.flatMap(dto -> orderOrchestratorEnrichmentService.enrichWithOrderProductBillingAndPayment(Mono.just(dto)));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderTransactionResponseDto> createOrder(@RequestBody OrderRequestDto order) {
        OrderTransactionResponseDto response;
        try {
            response = this.orderOrchestratorProducer.sendCreateOrderTransactionCommand(order);

            return orderOrchestratorEnrichmentService.enrichWithOrderProductBillingAndPaymentAndDiscount(Mono.just(response));
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to parse response: " + e.getMessage()));
        }
    }

    /**
     * Updates a line item.
     * This endpoint sends the update request to the order-service via Kafka.
     * After updating, it returns the enriched order transaction response.
     *
     * @param lineItem The line item data to update
     * @return The enriched order transaction response
     */
    @PutMapping(path = "/line-item", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderTransactionResponseDto> updateLineItem(@RequestBody LineItemRequestDto lineItem) {
        try {
            OrderTransactionResponseDto response = this.orderOrchestratorProducer.sendUpdateLineItemCommand(lineItem);

            // Get the order transaction by the order ID
            Mono<OrderTransactionResponseDto> orderTransactionResponseDtoMono =
                this.orderOrchestratorServiceClient.fetchOrderTransactionById(response.getId());

            return this.orderOrchestratorEnrichmentService.enrichWithOrderProductBillingAndPaymentAndDiscount(orderTransactionResponseDtoMono);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to update line item: " + e.getMessage()));
        }
    }

    @PostMapping(path = "/line-item", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderTransactionResponseDto> addLineItem(@RequestBody LineItemRequestDto lineItem) {
        try {
            OrderTransactionResponseDto responseDto = this.orderOrchestratorProducer.sendAddLineItemCommand(lineItem);

            // Get the order transaction by the order ID
            Mono<OrderTransactionResponseDto> orderTransactionResponseDtoMono =
                this.orderOrchestratorServiceClient.fetchOrderTransactionByOrderId(responseDto.getOrderId());

            return this.orderOrchestratorEnrichmentService.enrichWithOrderProductBillingAndPaymentAndDiscount(orderTransactionResponseDtoMono);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to add line item: " + e.getMessage()));
        }
    }

    @DeleteMapping(path = "/line-item/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderTransactionResponseDto> removeLineItem(@PathVariable String id) {
        LineItemRequestDto lineItemRequestDto = LineItemRequestDto.builder()
                .id(Long.valueOf(id))
                .build();

        try {
            OrderTransactionResponseDto response = this.orderOrchestratorProducer.sendRemoveLineItemCommand(lineItemRequestDto);

            // Get the order transaction by the order ID
            Mono<OrderTransactionResponseDto> orderTransactionResponseDtoMono =
                this.orderOrchestratorServiceClient.fetchOrderTransactionByOrderId(response.getOrderId());

            return this.orderOrchestratorEnrichmentService.enrichWithOrderProductBillingAndPaymentAndDiscount(orderTransactionResponseDtoMono);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to remove line item: " + e.getMessage()));
        }
    }

    @PostMapping(path = "/order/clear-line-items", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderTransactionResponseDto> clearLineItems(@RequestBody OrderRequestDto orderRequestDto) {
        try {
            OrderTransactionResponseDto response = this.orderOrchestratorProducer.sendClearLineItemsCommand(orderRequestDto);

            // Get the order transaction by the order ID
            Mono<OrderTransactionResponseDto> orderTransactionResponseDtoMono =
                this.orderOrchestratorServiceClient.fetchOrderTransactionByOrderId(response.getOrderId());

            return this.orderOrchestratorEnrichmentService.enrichWithOrderProductBillingAndPaymentAndDiscount(orderTransactionResponseDtoMono);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to clear line items: " + e.getMessage()));
        }
    }

    @PostMapping(path = "/apply-discount", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderTransactionResponseDto> applyDiscount(@RequestBody DiscountOrderRequestDto discountOrderRequestDto) {
        try {
            OrderTransactionResponseDto response = this.orderOrchestratorProducer.sendApplyDiscountOrderCommand(discountOrderRequestDto);

            // Get the order transaction by the order ID
            Mono<OrderTransactionResponseDto> orderTransactionResponseDtoMono =
                this.orderOrchestratorServiceClient.fetchOrderTransactionByOrderId(response.getOrderId());

            return this.orderOrchestratorEnrichmentService.enrichWithOrderProductBillingAndPaymentAndDiscount(orderTransactionResponseDtoMono);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to apply discount: " + e.getMessage()));
        }
    }

    @DeleteMapping(path = "/remove-line-item-discount", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderTransactionResponseDto> removeLineItemDiscount(@RequestBody DiscountOrderRequestDto discountOrderRequestDto) {
        try {
            OrderTransactionResponseDto response = this.orderOrchestratorProducer.sendRemoveLineItemDiscount(discountOrderRequestDto);

            // Get the order transaction by the order ID
            Mono<OrderTransactionResponseDto> orderTransactionResponseDtoMono =
                    this.orderOrchestratorServiceClient.fetchOrderTransactionByOrderId(response.getOrderId());

            return this.orderOrchestratorEnrichmentService.enrichWithOrderProductBillingAndPaymentAndDiscount(orderTransactionResponseDtoMono);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to remove line item discount: " + e.getMessage()));
        }
    }

    @DeleteMapping(path = "/remove-order-discount", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderTransactionResponseDto> removeOrderDiscount(@RequestBody DiscountOrderRequestDto discountOrderRequestDto) {
        try {
            OrderTransactionResponseDto response = this.orderOrchestratorProducer.sendRemoveOrderDiscount(discountOrderRequestDto);

            // Get the order transaction by the order ID
            Mono<OrderTransactionResponseDto> orderTransactionResponseDtoMono =
                    this.orderOrchestratorServiceClient.fetchOrderTransactionByOrderId(response.getOrderId());

            return this.orderOrchestratorEnrichmentService.enrichWithOrderProductBillingAndPaymentAndDiscount(orderTransactionResponseDtoMono);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to remove line item discount: " + e.getMessage()));
        }
    }
}
