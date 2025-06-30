package solutions.skydev.pos.discount_service.consumer;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.common.discount_service.dto.request.DiscountProductRequestDto;
import solutions.skydev.pos.common.discount_service.dto.response.DiscountProductResponseDto;
import solutions.skydev.pos.discount_service.model.entity.DiscountProduct;
import solutions.skydev.pos.discount_service.model.mapper.DiscountProductMapper;
import solutions.skydev.pos.discount_service.service.DiscountProductService;

// Todo: Working but not yet finished, not required for now.
@Component
public class DiscountProductConsumer {
    private final DiscountProductMapper discountProductMapper;
    private final DiscountProductService discountProductService;

    public DiscountProductConsumer(DiscountProductMapper discountProductMapper, DiscountProductService discountProductService) {
        this.discountProductMapper = discountProductMapper;
        this.discountProductService = discountProductService;
    }

    @KafkaListener(topics = "create-discount-product-command",
    properties = "spring.json.value.default.type=solutions.skydev.pos.common.discount_service.dto.request.DiscountProductRequestDto")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "create-discount-product-command",
            description = "Create discount-product command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("discount-product.created")
    public DiscountProductResponseDto createDiscountProductCommand(DiscountProductRequestDto discountProductRequestDto) {
        DiscountProduct discount = this.discountProductMapper.toEntity(discountProductRequestDto);
        discount = discountProductService.save(discount);
        return this.discountProductMapper.toDto(discount);
    }

    @KafkaListener(topics = "delete-discount-product-command",
    properties = "spring.json.value.default.type=solutions.skydev.pos.common.discount_service.dto.request.DiscountProductRequestDto")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "delete-discount-product-command",
            description = "delete discount-product command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("discount-product.deleted")
    public DiscountProductResponseDto deleteDiscountProduct(DiscountProductRequestDto discountProductRequestDto) {
        DiscountProduct discountProduct = this.discountProductMapper.toEntity(discountProductRequestDto);
        discountProduct = discountProductService.deleteById(discountProduct);
        return this.discountProductMapper.toDto(discountProduct);
    }
}
