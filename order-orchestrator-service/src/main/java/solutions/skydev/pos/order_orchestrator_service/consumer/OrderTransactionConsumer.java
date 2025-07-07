package solutions.skydev.pos.order_orchestrator_service.consumer;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.common.order_service.dto.request.OrderRequestDto;
import solutions.skydev.pos.common.order_service.dto.request.LineItemRequestDto;
import solutions.skydev.pos.common.order_orchestrator_service.dto.response.OrderTransactionResponseDto;
import solutions.skydev.pos.common.discount_service.dto.request.DiscountOrderRequestDto;
import solutions.skydev.pos.order_orchestrator_service.model.entity.OrderTransaction;
import solutions.skydev.pos.order_orchestrator_service.model.mapper.OrderTransactionMapper;
import solutions.skydev.pos.order_orchestrator_service.service.OrderTransactionService;

@Component
public class OrderTransactionConsumer {
    private final OrderTransactionService orderTransactionService;
    private final OrderTransactionMapper orderTransactionMapper;

    @Autowired
    public OrderTransactionConsumer(OrderTransactionService orderTransactionService, OrderTransactionMapper orderTransactionMapper) {
        this.orderTransactionService = orderTransactionService;
        this.orderTransactionMapper = orderTransactionMapper;
    }

    @KafkaListener(topics = "create-order-transaction-command")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "create-order-transaction-command",
            description = "Create order transaction command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("order-transaction.created")
    public OrderTransactionResponseDto createOrderCommand(ConsumerRecord<String, OrderRequestDto> record) {
        OrderRequestDto orderRequestDto = record.value();
        OrderTransaction orderTransaction = orderTransactionService.createOrderTransaction(orderRequestDto);
        return orderTransactionMapper.toResponseDto(orderTransaction);
    }

    @KafkaListener(topics = "update-order-line-item-command")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "update-order-line-item-command",
            description = "Update order line item command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("order-transaction.updated")
    public OrderTransactionResponseDto updateLineItemCommand(ConsumerRecord<String, LineItemRequestDto> record) {
        OrderTransaction orderTransaction = orderTransactionService.updateLineItem(record.value());
        return orderTransactionMapper.toResponseDto(orderTransaction);
    }

    @KafkaListener(topics = "add-order-line-item-command")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "update-order-line-item-command",
            description = "Update order line item command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("order-transaction.updated")
    public OrderTransactionResponseDto addLineItemCommand(ConsumerRecord<String, LineItemRequestDto> record) {
        OrderTransaction orderTransaction = orderTransactionService.addLineItem(record.value());
        return orderTransactionMapper.toResponseDto(orderTransaction);
    }

    @KafkaListener(topics = "remove-order-line-item-command")
    @SendTo("order-transaction.updated")
    public OrderTransactionResponseDto removeLineItemCommand(ConsumerRecord<String, LineItemRequestDto> record) {
        OrderTransaction orderTransaction = orderTransactionService.removeLineItem(record.value());
        return orderTransactionMapper.toResponseDto(orderTransaction);
    }

    @KafkaListener(topics = "clear-order-line-items-command")
    @SendTo("order-transaction.updated")
    public OrderTransactionResponseDto clearLineItemsCommand(ConsumerRecord<String, OrderRequestDto> record) {
        OrderTransaction orderTransaction = orderTransactionService.clearLineItems(record.value());
        return orderTransactionMapper.toResponseDto(orderTransaction);
    }

    @KafkaListener(topics = "apply-tagged-discount-command")
    @SendTo("order-transaction.updated")
    public OrderTransactionResponseDto applyDiscountOrderCommand(ConsumerRecord<String, DiscountOrderRequestDto> record) {
        OrderTransaction orderTransaction = orderTransactionService.applyDiscountOrder(record.value());
        return orderTransactionMapper.toResponseDto(orderTransaction);
    }

    @KafkaListener(topics = "delete-tagged-discount-order-command")
    @SendTo("order-transaction.updated")
    public OrderTransactionResponseDto deleteDiscountOrderCommand(ConsumerRecord<String, DiscountOrderRequestDto> record) {
        OrderTransaction orderTransaction = orderTransactionService.removeOrderDiscount(record.value());
        return orderTransactionMapper.toResponseDto(orderTransaction);
    }

    @KafkaListener(topics = "delete-tagged-line-item-discount-order-command")
    @SendTo("order-transaction.updated")
    public OrderTransactionResponseDto deleteLineItemDiscountOrderCommand(ConsumerRecord<String, DiscountOrderRequestDto> record) {
        OrderTransaction orderTransaction = orderTransactionService.removeLineItemDiscount(record.value());
        return orderTransactionMapper.toResponseDto(orderTransaction);
    }
}
