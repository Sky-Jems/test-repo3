package solutions.skydev.pos.order_orchestrator_service.consumer;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.order_orchestrator_service.model.dto.request.DiscountOrderRequestDto;
import solutions.skydev.pos.order_orchestrator_service.model.dto.request.LineItemRequestDto;
import solutions.skydev.pos.order_orchestrator_service.model.dto.request.OrderTransactionRequestDto;
import solutions.skydev.pos.order_orchestrator_service.model.dto.response.OrderTransactionResponseDto;
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

    @KafkaListener(topics = "create-order-transaction-command",
            properties = "spring.json.value.default.type=solutions.skydev.pos.order_orchestrator_service.model.dto.request.OrderTransactionRequestDto")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "create-order-transaction-command",
            description = "Create order transaction command",
            payloadType = OrderTransactionRequestDto.class
    ))
    @KafkaAsyncOperationBinding
    @SendTo("order-transaction.created")
    public OrderTransactionResponseDto createOrderCommand(ConsumerRecord<String, OrderTransactionRequestDto> record) {
        OrderTransactionRequestDto orderTransactionRequestDto = record.value();
        OrderTransaction orderTransaction = orderTransactionService.createOrderTransaction(orderTransactionRequestDto);
        return orderTransactionMapper.toResponseDto(orderTransaction);
    }

    @KafkaListener(topics = "update-order-line-item-command",
            properties = "spring.json.value.default.type=solutions.skydev.pos.order_orchestrator_service.model.dto.request.LineItemRequestDto")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "update-order-line-item-command",
            description = "Update order line item command",
            payloadType = OrderTransactionRequestDto.class
    ))
    @KafkaAsyncOperationBinding
    @SendTo("order-transaction.updated")
    public OrderTransactionResponseDto updateLineItemCommand(ConsumerRecord<String, LineItemRequestDto> record) {
        OrderTransaction orderTransaction = orderTransactionService.updateLineItem(record.value());
        return orderTransactionMapper.toResponseDto(orderTransaction);
    }

    @KafkaListener(topics = "add-order-line-item-command",
            properties = "spring.json.value.default.type=solutions.skydev.pos.order_orchestrator_service.model.dto.request.LineItemRequestDto")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "update-order-line-item-command",
            description = "Update order line item command",
            payloadType = OrderTransactionRequestDto.class
    ))
    @KafkaAsyncOperationBinding
    @SendTo("order-transaction.updated")
    public OrderTransactionResponseDto addLineItemCommand(ConsumerRecord<String, LineItemRequestDto> record) {
        OrderTransaction orderTransaction = orderTransactionService.addLineItem(record.value());
        return orderTransactionMapper.toResponseDto(orderTransaction);
    }

    @KafkaListener(topics = "remove-order-line-item-command",
            properties = "spring.json.value.default.type=solutions.skydev.pos.order_orchestrator_service.model.dto.request.LineItemRequestDto")
    @SendTo("order-transaction.updated")
    public OrderTransactionResponseDto removeLineItemCommand(ConsumerRecord<String, LineItemRequestDto> record) {
        OrderTransaction orderTransaction = orderTransactionService.removeLineItem(record.value());
        return orderTransactionMapper.toResponseDto(orderTransaction);
    }
    
    @KafkaListener(topics = "tag-discount-command",
            properties = "spring.json.value.default.type=solutions.skydev.pos.order_orchestrator_service.model.dto.request.DiscountOrderRequestDto")
    @SendTo("order-transaction.updated")
    public OrderTransactionResponseDto tagDiscountCommand(ConsumerRecord<String, DiscountOrderRequestDto> record) {
        OrderTransaction orderTransaction = orderTransactionService.tagDiscount(record.value());
        return orderTransactionMapper.toResponseDto(orderTransaction);
    }
}
