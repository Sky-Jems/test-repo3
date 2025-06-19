package solutions.skydev.pos.discount_service.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.discount_service.model.dto.request.ApplyDiscountRequestDto;
import solutions.skydev.pos.discount_service.model.dto.request.DiscountRequestDto;
import solutions.skydev.pos.discount_service.model.dto.response.ApplyDiscountResponseDto;
import solutions.skydev.pos.discount_service.model.dto.response.DiscountResponseDto;
import solutions.skydev.pos.discount_service.model.entity.Discount;
import solutions.skydev.pos.discount_service.model.entity.DiscountLineItem;
import solutions.skydev.pos.discount_service.model.entity.Order;
import solutions.skydev.pos.discount_service.model.mapper.DiscountMapper;
import solutions.skydev.pos.discount_service.service.DiscountService;

@Component
public class DiscountConsumer {
    private final DiscountService discountService;
    private final DiscountMapper discountMapper;

    @Autowired
    public DiscountConsumer(DiscountService discountService, DiscountMapper discountMapper) {
        this.discountService = discountService;
        this.discountMapper = discountMapper;
    }

    @KafkaListener(topics = "apply-discount-command",
    properties = "spring.json.value.default.type=solutions.skydev.pos.discount_service.model.dto.request.ApplyDiscountRequestDto")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "apply-discount-command",
            description = "Apply discount command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("discount.applied")
    public ApplyDiscountResponseDto applyDiscountCommand(ApplyDiscountRequestDto applyDiscountRequestDto) {
        Discount discount = this.discountMapper.toEntity(applyDiscountRequestDto);
        Order order = this.discountMapper.toOrderEntity(applyDiscountRequestDto);
        DiscountLineItem discountLineItem = discountService.applyDiscount(order, discount);
        return discountMapper.toDto(discountLineItem);
    }

    @KafkaListener(topics = "create-discount-command",
    properties = "spring.json.value.default.type=solutions.skydev.pos.discount_service.model.dto.request.DiscountRequestDto")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "create-discount-command",
            description = "Create discount command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("discount.created")
    public DiscountResponseDto createDiscountCommand(DiscountRequestDto discountRequestDto) {
        Discount discount = this.discountMapper.toEntity(discountRequestDto);
        return this.discountMapper.toDto(
                this.discountService.createDiscount(discount)
        );
    }

    @KafkaListener(topics = "update-discount-command",
    properties = "spring.json.value.default.type=solutions.skydev.pos.discount_service.model.dto.request.DiscountRequestDto")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "update-discount-command",
            description = "Update discount command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("discount.updated")
    public DiscountResponseDto updateDiscountCommand(DiscountRequestDto discountRequestDto) {
        Discount discount = this.discountMapper.toEntity(discountRequestDto);
        return this.discountMapper.toDto(
                this.discountService.updateDiscount(discount)
        );
    }

    @KafkaListener(topics = "delete-discount-command",
    properties = "spring.json.value.default.type=solutions.skydev.pos.discount_service.model.dto.request.DiscountRequestDto")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "delete-discount-command",
            description = "Delete discount command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("discount.deleted")
    public DiscountResponseDto deleteDiscountCommand(DiscountRequestDto discountRequestDto) {
        Discount discount = this.discountMapper.toEntity(discountRequestDto);
        discount = this.discountService.deleteDiscount(discount.getId());
        return this.discountMapper.toDto(discount);
    }
}
