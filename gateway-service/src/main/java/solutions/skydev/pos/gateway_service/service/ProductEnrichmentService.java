package solutions.skydev.pos.gateway_service.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import solutions.skydev.pos.gateway_service.model.dto.response.*;

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

    /**
     * Enriches order line items with product details.
     * For each line item, fetches the associated variant and its combinations.
     *
     * @param orderResponseMono Mono containing the order to be enriched
     * @return Mono containing the enriched order
     */
    public Mono<OrderResponseDto> enrichOrderLineItemsWithProductDetails(Mono<OrderResponseDto> orderResponseMono) {
        return orderResponseMono.flatMap(this::enrichOrderLineItems);
    }

    /**
     * Enriches the line items of an order with product details.
     *
     * @param orderResponse The order response to enrich
     * @return Mono containing the enriched order
     */
    private Mono<OrderResponseDto> enrichOrderLineItems(OrderResponseDto orderResponse) {
        List<LineItemResponseDto> lineItems = orderResponse.getLineItems();
        
        return Flux.fromIterable(lineItems)
                .flatMap(this::enrichLineItemWithVariant)
                .collectList()
                .map(enrichedLineItems -> {
                    orderResponse.setLineItems(enrichedLineItems);
                    return orderResponse;
                });
    }

    /**
     * Enriches a single line item with variant details.
     *
     * @param lineItem The line item to enrich
     * @return Mono containing the enriched line item
     */
    private Mono<LineItemResponseDto> enrichLineItemWithVariant(LineItemResponseDto lineItem) {
        return productServiceClient.getVariantById(lineItem.getProductId())
                .flatMap(variant -> enrichVariantWithCombinations(variant)
                        .map(enrichedVariant -> {
                            lineItem.setVariant(enrichedVariant);
                            return lineItem;
                        }))
                .onErrorResume(e -> {
                    // Log the error and return the original line item
                    System.err.println("Error enriching line item with variant: " + e.getMessage());
                    return Mono.just(lineItem);
                });
    }

    /**
     * Enriches a variant with its combinations (variant option values and options).
     *
     * @param variant The variant to enrich
     * @return Mono containing the enriched variant
     */
    public Mono<VariantResponseDto> enrichVariantWithCombinations(VariantResponseDto variant) {
        return productServiceClient.getVariantAssignmentsByVariantId(variant.getVariantId())
                .flatMap(this::enrichVariantAssignmentWithOptionValue)
                .collectList()
                .map(variantCombinations -> {
                    variant.setVariantCombinations(variantCombinations);
                    return variant;
                });
    }

    /**
     * Enriches a variant assignment with its option value and option.
     *
     * @param assignment The variant assignment to enrich
     * @return Mono containing the enriched variant option value
     */
    private Mono<VariantOptionValueResponseDto> enrichVariantAssignmentWithOptionValue(ProductVariantAssignmentResponseDto assignment) {
        return productServiceClient.getVariantOptionValue(assignment.getVariantOptionValueId())
                .flatMap(this::enrichVariantOptionValueWithOption);
    }

    /**
     * Enriches a variant option value with its option.
     *
     * @param variantOptionValue The variant option value to enrich
     * @return Mono containing the enriched variant option value
     */
    private Mono<VariantOptionValueResponseDto> enrichVariantOptionValueWithOption(VariantOptionValueResponseDto variantOptionValue) {
        return productServiceClient.getVariantOptionById(variantOptionValue.getVariantOptionId())
                .map(variantOption -> {
                    variantOptionValue.setVariantOption(variantOption);
                    return variantOptionValue;
                });
    }
}