package solutions.skydev.pos.order_service.consumer;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.common.order_service.dto.request.OrderRequestDto;
import solutions.skydev.pos.common.order_service.dto.response.OrderResponseDto;
import solutions.skydev.pos.order_service.model.entity.Order;
import solutions.skydev.pos.order_service.model.mapper.OrderMapper;
import solutions.skydev.pos.order_service.service.OrderService;

@Component
public class OrderConsumer {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    public OrderConsumer(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @KafkaListener(topics = "create-order-command")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "create-order-command",
            description = "Create order command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("order.created")
    public OrderResponseDto createOrderCommand(ConsumerRecord<String, OrderRequestDto> record) {
        OrderRequestDto orderRequestDto = record.value();
        Order order = this.orderMapper.toEntity(orderRequestDto);
        Order createdOrder = this.orderService.createOrder(order);
        return this.orderMapper.toResponseDto(createdOrder);
    }

    @KafkaListener(topics = "clear-line-items-command")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "clear-line-items-command",
            description = "Delete all line items from order command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("order.updated")
    public OrderResponseDto clearLineItemsCommand(ConsumerRecord<String, OrderRequestDto> record) {
        OrderRequestDto orderRequestDto = record.value();
        Order order = this.orderMapper.toEntity(orderRequestDto);
        Order clearedOrder = this.orderService.clearLineItems(order);
        return this.orderMapper.toResponseDto(clearedOrder);
    }

    @KafkaListener(topics = "update-customer-command")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "update-customer-command",
            description = "Update customer name command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("order.updated")
    public OrderResponseDto updateCustomerCommand(ConsumerRecord<String, OrderRequestDto> record) {
        OrderRequestDto orderRequestDto = record.value();
        Order order = this.orderMapper.toEntity(orderRequestDto);
        Order updatedOrder = this.orderService.updateCustomer(order);
        return this.orderMapper.toResponseDto(updatedOrder);
    }
}
