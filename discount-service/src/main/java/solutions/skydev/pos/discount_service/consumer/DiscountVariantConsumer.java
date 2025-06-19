package solutions.skydev.pos.discount_service.consumer;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.discount_service.model.dto.request.DiscountVariantRequestDto;
import solutions.skydev.pos.discount_service.model.dto.response.DiscountVariantResponseDto;
import solutions.skydev.pos.discount_service.model.entity.DiscountVariant;
import solutions.skydev.pos.discount_service.model.mapper.DiscountVariantMapper;
import solutions.skydev.pos.discount_service.service.DiscountVariantService;

// Todo: Working but not yet finished, not required for now.
@Component
public class DiscountVariantConsumer {
    private final DiscountVariantMapper discountVariantMapper;
    private final DiscountVariantService discountVariantService;

    public DiscountVariantConsumer(DiscountVariantMapper discountVariantMapper, DiscountVariantService discountVariantService) {
        this.discountVariantMapper = discountVariantMapper;
        this.discountVariantService = discountVariantService;
    }

    @KafkaListener(topics = "create-discount-variant-command",
    properties = "spring.json.value.default.type=solutions.skydev.pos.discount_service.model.dto.request.DiscountVariantRequestDto")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "create-discount-variant-command",
            description = "Create discount-variant command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("discount-variant.created")
    public DiscountVariantResponseDto createDiscountVariantCommand(DiscountVariantRequestDto discountVariantRequestDto) {
        DiscountVariant discount = this.discountVariantMapper.toEntity(discountVariantRequestDto);
        discount = discountVariantService.save(discount);
        return this.discountVariantMapper.toDto(discount);
    }

    @KafkaListener(topics = "delete-discount-variant-command",
    properties = "spring.json.value.default.type=solutions.skydev.pos.discount_service.model.dto.request.DiscountVariantRequestDto")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "delete-discount-variant-command",
            description = "delete discount-variant command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("discount-variant.deleted")
    public DiscountVariantResponseDto deleteDiscountVariant(DiscountVariantRequestDto discountVariantRequestDto) {
        DiscountVariant discountVariant = this.discountVariantMapper.toEntity(discountVariantRequestDto);
        discountVariant = discountVariantService.deleteById(discountVariant);
        return this.discountVariantMapper.toDto(discountVariant);
    }
}
