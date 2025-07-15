package solutions.skydev.pos.discount_service.consumer;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.common.discount_service.dto.request.DiscountOrderRequestDto;
import solutions.skydev.pos.common.discount_service.dto.response.DiscountOrderUpdatedResponseDto;
import solutions.skydev.pos.discount_service.model.DiscountOrderSummary;
import solutions.skydev.pos.discount_service.model.entity.LineItemLevelDiscountOrder;
import solutions.skydev.pos.discount_service.model.entity.Order;
import solutions.skydev.pos.discount_service.model.entity.OrderLevelDiscountOrder;
import solutions.skydev.pos.discount_service.model.mapper.DiscountOrderMapper;
import solutions.skydev.pos.discount_service.model.mapper.LineItemLevelDiscountOrderMapper;
import solutions.skydev.pos.discount_service.service.DiscountOrderService;

import java.util.List;

@Slf4j
@Component
public class DiscountOrderConsumer {
    private final DiscountOrderMapper discountOrderMapper;
    private final DiscountOrderService discountOrderService;

    @Autowired
    public DiscountOrderConsumer(DiscountOrderMapper discountOrderMapper, DiscountOrderService discountOrderService,
                                 LineItemLevelDiscountOrderMapper lineItemLevelDiscountOrderMapper) {
        this.discountOrderService = discountOrderService;
        this.discountOrderMapper = discountOrderMapper;
    }

    @KafkaListener(topics = "apply-discount-order-command",
    properties = "spring.json.value.default.type=solutions.skydev.pos.common.discount_service.dto.request.DiscountOrderRequestDto")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "apply-discount-order-command",
            description = "Apply discount order command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("discount-order.updated")
    public DiscountOrderUpdatedResponseDto applyDiscountOrderCommand(DiscountOrderRequestDto discountOrderRequestDto) {
        if (discountOrderRequestDto.getTotalAmount() != null && discountOrderRequestDto.getDiscountId() != null) {
            OrderLevelDiscountOrder entity = discountOrderMapper.toOrderLevelEntity(discountOrderRequestDto);
            Order order = discountOrderMapper.toOrderEntity(discountOrderRequestDto);
            discountOrderService.create(entity, order);
        } else if (discountOrderRequestDto.getLineItems() != null && !discountOrderRequestDto.getLineItems().isEmpty()) {
            List<LineItemLevelDiscountOrder> entities =
                    discountOrderMapper.toLineItemLevelEntities(discountOrderRequestDto.getLineItems(), discountOrderRequestDto.getOrderId());
            discountOrderService.create(entities, discountOrderRequestDto.getOrderId());
        } else {
            log.error("Invalid discount order request: {}", discountOrderRequestDto);
            throw new IllegalArgumentException("Invalid discount order request: " + discountOrderRequestDto);
        }

        DiscountOrderSummary summary = discountOrderService.getDiscountOrderSummary(discountOrderRequestDto.getOrderId());
        return discountOrderMapper.toDto(summary);
    }

    @KafkaListener(topics = "delete-discount-order-command",
    properties = "spring.json.value.default.type=solutions.skydev.pos.common.discount_service.dto.request.DiscountOrderRequestDto")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "delete-discount-order-command",
            description = "Delete discount order command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("discount-order.updated")
    public DiscountOrderUpdatedResponseDto deleteDiscountOrderCommand(DiscountOrderRequestDto discountOrderRequestDto) {
        if (discountOrderRequestDto.getOrderId() == null) {
            log.error("Order ID cannot be null in delete discount order command");
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        discountOrderService.deleteByOrderId(discountOrderRequestDto.getOrderId());
        DiscountOrderSummary summary = discountOrderService.getDiscountOrderSummary(discountOrderRequestDto.getOrderId());
        return discountOrderMapper.toDto(summary);
    }

    @KafkaListener(topics = "delete-discount-order-line-item-command",
            properties = "spring.json.value.default.type=solutions.skydev.pos.common.discount_service.dto.request.DiscountOrderRequestDto")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "delete-discount-order-line-item-command",
            description = "delete-discount-order-line-item-command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("discount-order.updated")
    public DiscountOrderUpdatedResponseDto deleteDiscountOrderLineItemCommand(DiscountOrderRequestDto discountOrderRequestDto) {
        if (discountOrderRequestDto.getLineItems() == null || discountOrderRequestDto.getLineItems().isEmpty()) {
            log.error("Line items cannot be null or empty in delete discount order line item command");
            throw new IllegalArgumentException("Line items cannot be null or empty");
        }

        discountOrderService.deleteByLineItems(
                discountOrderRequestDto.getOrderId(),
                discountOrderMapper.toLineItemLevelEntities(discountOrderRequestDto.getLineItems(), discountOrderRequestDto.getOrderId())
        );

        DiscountOrderSummary summary = discountOrderService.getDiscountOrderSummary(discountOrderRequestDto.getOrderId());
        return discountOrderMapper.toDto(summary);
    }
}
