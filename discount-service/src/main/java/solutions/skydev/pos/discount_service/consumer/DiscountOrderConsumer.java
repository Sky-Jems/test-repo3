package solutions.skydev.pos.discount_service.consumer;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.discount_service.model.dto.request.DiscountOrderRequestDto;
import solutions.skydev.pos.discount_service.model.dto.response.DiscountOrderResponseDto;
import solutions.skydev.pos.discount_service.model.entity.DiscountOrder;
import solutions.skydev.pos.discount_service.model.mapper.DiscountOrderMapper;
import solutions.skydev.pos.discount_service.service.DiscountOrderService;

@Component
public class DiscountOrderConsumer {
    private final DiscountOrderMapper discountOrderMapper;
    private final DiscountOrderService discountOrderService;

    @Autowired
    public DiscountOrderConsumer(DiscountOrderMapper discountOrderMapper, DiscountOrderService discountOrderService) {
        this.discountOrderService = discountOrderService;
        this.discountOrderMapper = discountOrderMapper;
    }

    @KafkaListener(topics = "create-discount-order-command",
    properties = "spring.json.value.default.type=solutions.skydev.pos.discount_service.model.dto.request.DiscountOrderRequestDto")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "create-discount-order-command",
            description = "Create discount order command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("discount-order.created")
    public DiscountOrderResponseDto createDiscountOrderCommand(DiscountOrderRequestDto discountOrderRequestDto) {
        DiscountOrder discountOrder = this.discountOrderMapper.toEntity(discountOrderRequestDto);
        return this.discountOrderMapper.toDto(
                this.discountOrderService.create(discountOrder)
        );
    }

    @KafkaListener(topics = "delete-discount-order-command",
    properties = "spring.json.value.default.type=solutions.skydev.pos.discount_service.model.dto.request.DiscountOrderRequestDto")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "delete-discount-order-command",
            description = "Delete discount order command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("discount-order.deleted")
    public DiscountOrderResponseDto deleteOrderDiscountCommand(DiscountOrderRequestDto discountOrderRequestDto) {
        DiscountOrder discountOrder = this.discountOrderMapper.toEntity(discountOrderRequestDto);
        return this.discountOrderMapper.toDto(
                this.discountOrderService.deleteByOrderId(discountOrder.getOrderId())
        );
    }
}
