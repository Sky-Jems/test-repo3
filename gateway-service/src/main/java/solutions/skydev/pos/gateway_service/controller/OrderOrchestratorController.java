package solutions.skydev.pos.gateway_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import solutions.skydev.pos.gateway_service.model.dto.request.DiscountOrderRequestDto;
import solutions.skydev.pos.gateway_service.model.dto.request.LineItemRequestDto;
import solutions.skydev.pos.gateway_service.model.dto.response.*;
import solutions.skydev.pos.gateway_service.producer.OrderOrchestratorProducer;
import solutions.skydev.pos.gateway_service.service.OrderOrchestratorServiceClient;
import solutions.skydev.pos.gateway_service.service.OrderServiceClient;
import solutions.skydev.pos.gateway_service.service.ProductEnrichmentService;
import solutions.skydev.pos.gateway_service.service.ProductServiceClient;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/order-transaction")
public class OrderOrchestratorController {
    private final OrderOrchestratorProducer orderOrchestratorProducer;

    private final ProductServiceClient productServiceClient;
    private final OrderServiceClient orderServiceClient;
    private final OrderOrchestratorServiceClient orderOrchestratorServiceClient;
    private final ProductEnrichmentService productEnrichmentService;

    private final ObjectMapper objectMapper;

    @Autowired
    public OrderOrchestratorController(OrderOrchestratorProducer orderOrchestratorProducer, 
                                       OrderServiceClient orderServiceClient, 
                                       OrderOrchestratorServiceClient orderOrchestratorServiceClient, 
                                       ProductServiceClient productServiceClient,
                                       ProductEnrichmentService productEnrichmentService) {
        this.orderOrchestratorProducer = orderOrchestratorProducer;
        this.objectMapper = new ObjectMapper();
        this.orderServiceClient = orderServiceClient;
        this.orderOrchestratorServiceClient = orderOrchestratorServiceClient;
        this.productServiceClient = productServiceClient;
        this.productEnrichmentService = productEnrichmentService;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderTransactionResponseDto> getOrderTransactionById(@PathVariable String id) {

        Mono<OrderTransactionResponseDto> orderTransactionResponseDtoMono = this.orderOrchestratorServiceClient.fetchOrderTransactionById(Long.valueOf(id));
        return orderTransactionResponseDtoMono.flatMap(orderTransactionResponseDto -> {
            Mono<OrderResponseDto> orderResponseMono = this.orderServiceClient.fetchOrderById(String.valueOf(orderTransactionResponseDto.getOrderId()));
            return Mono.zip(
                    Mono.just(orderTransactionResponseDto),
                    enrichOrderLineItemsWithProductDetails(orderResponseMono),
                    (transactionResponse, orderResponse) -> {
                        transactionResponse.setOrder(orderResponse);
                        return transactionResponse;
                    }
            ).onErrorResume(e -> Mono.error(new RuntimeException("Error fetching order: " + e.getMessage())));
        });
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
        return orderTransactionResponseDtoMono.flatMap(orderTransactionResponseDto -> {
            Mono<OrderResponseDto> orderResponseMono = this.orderServiceClient.fetchOrderById(String.valueOf(orderTransactionResponseDto.getOrderId()));
            return Mono.zip(
                    Mono.just(orderTransactionResponseDto),
                    enrichOrderLineItemsWithProductDetails(orderResponseMono),
                    (transactionResponse, orderResponse) -> {
                        transactionResponse.setOrder(orderResponse);
                        return transactionResponse;
                    }
            ).onErrorResume(e -> Mono.error(new RuntimeException("Error fetching order: " + e.getMessage())));
        });
    }

    /**
     * Enriches order line items with product details.
     * Delegates to the ProductEnrichmentService to fetch and attach product details to each line item.
     *
     * @param orderResponseMono Mono containing the order to be enriched
     * @return Mono containing the enriched order
     */
    private Mono<OrderResponseDto> enrichOrderLineItemsWithProductDetails(Mono<OrderResponseDto> orderResponseMono) {
        return productEnrichmentService.enrichOrderLineItemsWithProductDetails(orderResponseMono);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderTransactionResponseDto> createOrder(@RequestBody String order) {
        String response = this.orderOrchestratorProducer.sendCreateOrderTransactionCommand(order);
        OrderTransactionResponseDto responseDto;
        try {
            responseDto = this.objectMapper.readValue(response, OrderTransactionResponseDto.class);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to parse response: " + e.getMessage()));
        }

        Mono<OrderResponseDto> orderResponseMono = this.orderServiceClient.fetchOrderById(String.valueOf(responseDto.getOrderId()));
        return Mono.zip(
                Mono.just(responseDto),
                enrichOrderLineItemsWithProductDetails(orderResponseMono),
                (transactionResponse, orderResponse) -> {
                    transactionResponse.setOrder(orderResponse);
                    return transactionResponse;
                }
        ).onErrorResume(e -> Mono.error(new RuntimeException("Error fetching order: " + e.getMessage())));
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
            String requestBody = this.objectMapper.writeValueAsString(lineItem);
            String response = this.orderOrchestratorProducer.sendUpdateLineItemCommand(requestBody);
            System.out.println("Response from update line item command: " + response);
            OrderTransactionResponseDto responseDto = this.objectMapper.readValue(response, OrderTransactionResponseDto.class);

            // Get the order transaction by the order ID
            Mono<OrderTransactionResponseDto> orderTransactionResponseDtoMono = 
                this.orderOrchestratorServiceClient.fetchOrderTransactionById(responseDto.getId());

            return orderTransactionResponseDtoMono.flatMap(orderTransactionResponseDto -> {
                Mono<OrderResponseDto> orderResponseMono = 
                    this.orderServiceClient.fetchOrderById(String.valueOf(orderTransactionResponseDto.getOrderId()));
                return Mono.zip(
                        Mono.just(orderTransactionResponseDto),
                        enrichOrderLineItemsWithProductDetails(orderResponseMono),
                        (transactionResponse, orderResponse) -> {
                            transactionResponse.setOrder(orderResponse);
                            return transactionResponse;
                        }
                ).onErrorResume(e -> Mono.error(new RuntimeException("Error fetching order: " + e.getMessage())));
            });
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to update line item: " + e.getMessage()));
        }
    }

    @DeleteMapping(path = "/line-item", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderTransactionResponseDto> removeLineItem(@RequestBody LineItemRequestDto lineItem) {
        try {
            String requestBody = this.objectMapper.writeValueAsString(lineItem);
            String response = this.orderOrchestratorProducer.sendRemoveLineItemCommand(requestBody);
            OrderTransactionResponseDto responseDto = this.objectMapper.readValue(response, OrderTransactionResponseDto.class);

            // Get the order transaction by the order ID
            Mono<OrderTransactionResponseDto> orderTransactionResponseDtoMono =
                    this.orderOrchestratorServiceClient.fetchOrderTransactionById(responseDto.getId());

            return orderTransactionResponseDtoMono.flatMap(orderTransactionResponseDto -> {
                Mono<OrderResponseDto> orderResponseMono =
                        this.orderServiceClient.fetchOrderById(String.valueOf(orderTransactionResponseDto.getOrderId()));
                return Mono.zip(
                        Mono.just(orderTransactionResponseDto),
                        enrichOrderLineItemsWithProductDetails(orderResponseMono),
                        (transactionResponse, orderResponse) -> {
                            transactionResponse.setOrder(orderResponse);
                            return transactionResponse;
                        }
                ).onErrorResume(e -> Mono.error(new RuntimeException("Error fetching order: " + e.getMessage())));
            });
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to update line item: " + e.getMessage()));
        }
    }

    /**
     * Adds a line item to an order.
     * This endpoint sends the add request to the order-service via Kafka.
     * After adding, it returns the enriched order transaction response.
     *
     * @param lineItem The line item data to add
     * @return The enriched order transaction response
     */
    @PostMapping(path = "/line-item", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderTransactionResponseDto> addLineItem(@RequestBody LineItemRequestDto lineItem) {
        try {
            String requestBody = this.objectMapper.writeValueAsString(lineItem);
            String response = this.orderOrchestratorProducer.sendAddLineItemCommand(requestBody);
            OrderTransactionResponseDto responseDto = this.objectMapper.readValue(response, OrderTransactionResponseDto.class);

            // Get the order transaction by the order ID
            Mono<OrderTransactionResponseDto> orderTransactionResponseDtoMono = 
                this.orderOrchestratorServiceClient.fetchOrderTransactionByOrderId(responseDto.getOrderId());

            return orderTransactionResponseDtoMono.flatMap(orderTransactionResponseDto -> {
                Mono<OrderResponseDto> orderResponseMono = 
                    this.orderServiceClient.fetchOrderById(String.valueOf(orderTransactionResponseDto.getOrderId()));
                return Mono.zip(
                        Mono.just(orderTransactionResponseDto),
                        enrichOrderLineItemsWithProductDetails(orderResponseMono),
                        (transactionResponse, orderResponse) -> {
                            transactionResponse.setOrder(orderResponse);
                            return transactionResponse;
                        }
                ).onErrorResume(e -> Mono.error(new RuntimeException("Error fetching order: " + e.getMessage())));
            });
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to add line item: " + e.getMessage()));
        }
    }


    @PostMapping(path = "/discount", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderTransactionResponseDto> tagDiscount(@RequestBody DiscountOrderRequestDto discountOrderRequestDto) {
        try {
            String requestBody = this.objectMapper.writeValueAsString(discountOrderRequestDto);
            String response = this.orderOrchestratorProducer.sendTagOrderDiscountCommand(requestBody);
            OrderTransactionResponseDto responseDto = this.objectMapper.readValue(response, OrderTransactionResponseDto.class);

            // Get the order transaction by the order ID
            Mono<OrderTransactionResponseDto> orderTransactionResponseDtoMono =
                    this.orderOrchestratorServiceClient.fetchOrderTransactionByOrderId(responseDto.getOrderId());

            return orderTransactionResponseDtoMono.flatMap(orderTransactionResponseDto -> {
                Mono<OrderResponseDto> orderResponseMono =
                        this.orderServiceClient.fetchOrderById(String.valueOf(orderTransactionResponseDto.getOrderId()));
                return Mono.zip(
                        Mono.just(orderTransactionResponseDto),
                        enrichOrderLineItemsWithProductDetails(orderResponseMono),
                        (transactionResponse, orderResponse) -> {
                            transactionResponse.setOrder(orderResponse);
                            return transactionResponse;
                        }
                ).onErrorResume(e -> Mono.error(new RuntimeException("Error fetching order: " + e.getMessage())));
            });
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to discount order command: " + e.getMessage()));
        }
    } 
}
