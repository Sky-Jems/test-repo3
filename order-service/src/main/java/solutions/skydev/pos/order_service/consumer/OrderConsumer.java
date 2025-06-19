package solutions.skydev.pos.order_service.consumer;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.order_service.model.dto.request.OrderRequestDto;
import solutions.skydev.pos.order_service.model.dto.response.OrderResponseDto;
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

    @KafkaListener(topics = "create-order-command",
            properties = "spring.json.value.default.type=solutions.skydev.pos.order_service.model.dto.request.OrderRequestDto")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "create-order-command",
            description = "Create order command",
            payloadType = OrderRequestDto.class
    ))
    @KafkaAsyncOperationBinding
    @SendTo("order.created")
    public OrderResponseDto createOrderCommand(ConsumerRecord<String, OrderRequestDto> record) {
        OrderRequestDto orderRequestDto = record.value();
        Order order = this.orderMapper.toEntity(orderRequestDto);
        Order createdOrder = this.orderService.createOrder(order);
        return this.orderMapper.toResponseDto(createdOrder);
    }
}
