package solutions.skydev.pos.gateway_service.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import solutions.skydev.pos.common.order_service.dto.response.LineItemResponseDto;
import solutions.skydev.pos.common.order_service.dto.response.OrderResponseDto;
import solutions.skydev.pos.common.product_service.dto.response.ProductResponseDto;

import java.util.Comparator;
import java.util.List;

/**
 * Service responsible for enriching order data with product details.
 * This service handles the fetching and mapping of product-related information
 * to enhance order line items with complete product details.
 */
@Service
public class ProductEnrichmentService {

    private final ProductServiceClient productServiceClient;

    public ProductEnrichmentService(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    private Mono<ProductResponseDto> enrichProductWithCategories(ProductResponseDto product) {
        return Flux.fromIterable(product.getCategoryIds())
                .flatMap(categoryId -> productServiceClient.getCategoryById(categoryId)
                        .onErrorResume(throwable -> Mono.empty())) // Handle error gracefully
                .collectList()
                .map(categories -> {
                    product.setCategories(categories);
                    return product;
                })
                .onErrorResume(throwable -> Mono.just(product)); // Return product even if categories cannot be fetched
    }

    private Mono<LineItemResponseDto> enrichLineItemWithProduct(LineItemResponseDto lineItem) {
        return productServiceClient.getById(lineItem.getProductId())
                .flatMap(this::enrichProductWithCategories)
                .map(productResponseDto ->  {
                    lineItem.setProduct(productResponseDto);
                    return lineItem;
                })
                .onErrorResume(throwable -> Mono.just(lineItem));
    }

    public Mono<OrderResponseDto> enrichOrderLineItems(Mono<OrderResponseDto> orderResponse) {
        return orderResponse.flatMap(order -> {
            List<LineItemResponseDto> lineItems = order.getLineItems();
            if (lineItems == null || lineItems.isEmpty()) {
                return Mono.just(order); // No line items to enrich
            }

            return Flux.fromIterable(lineItems)
                    .flatMap(this::enrichLineItemWithProduct)
                    .collectList()
                    .map(enrichedLineItems -> {
                        enrichedLineItems.sort(Comparator.comparing(
                                LineItemResponseDto::getCreatedAt,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        ).reversed());

                        order.setLineItems(enrichedLineItems);
                        return order;
                    });

        });
    }
}
