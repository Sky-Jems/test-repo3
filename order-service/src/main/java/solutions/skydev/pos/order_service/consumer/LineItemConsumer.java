package solutions.skydev.pos.order_service.consumer;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.order_service.model.dto.request.LineItemRequestDto;
import solutions.skydev.pos.order_service.model.dto.response.LineItemResponseDto;
import solutions.skydev.pos.order_service.model.dto.response.OrderResponseDto;
import solutions.skydev.pos.order_service.model.entity.LineItem;
import solutions.skydev.pos.order_service.model.entity.Order;
import solutions.skydev.pos.order_service.model.mapper.LineItemMapper;
import solutions.skydev.pos.order_service.model.mapper.OrderMapper;
import solutions.skydev.pos.order_service.service.LineItemService;

@Component
public class LineItemConsumer {
    private final LineItemService lineItemService;
    private final LineItemMapper lineItemMapper;
    private final OrderMapper orderMapper;

    @Autowired
    public LineItemConsumer(LineItemService lineItemService, LineItemMapper lineItemMapper, OrderMapper orderMapper) {
        this.lineItemService = lineItemService;
        this.lineItemMapper = lineItemMapper;
        this.orderMapper =  orderMapper;
    }

    @KafkaListener(topics = "add-line-item-command",
            properties = "spring.json.value.default.type=solutions.skydev.pos.order_service.model.dto.request.LineItemRequestDto")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "add-line-item-command",
            description = "Add line item command",
            payloadType = LineItemRequestDto.class
    ))
    @KafkaAsyncOperationBinding
    @SendTo("order.updated")
    public OrderResponseDto addLineItemCommand(LineItemRequestDto lineItemRequestDto) {
        LineItem lineItem = this.lineItemMapper.toEntityFromAdd(lineItemRequestDto);
        Order order = this.lineItemService.saveLineItem(lineItem);
        return this.orderMapper.toResponseDto(order);
    }

    @KafkaListener(topics = "update-line-item-command",
            properties = "spring.json.value.default.type=solutions.skydev.pos.order_service.model.dto.request.LineItemRequestDto")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "add-line-item-command",
            description = "Add line item command",
            payloadType = LineItemRequestDto.class
    ))
    @KafkaAsyncOperationBinding
    @SendTo("order.updated")
    public OrderResponseDto updateLineItemCommand(LineItemRequestDto lineItemRequestDto) {
        LineItem lineItem = this.lineItemMapper.toEntityFromUpdate(lineItemRequestDto);
        Order updatedOrder = this.lineItemService.updateLineItem(lineItem);
        return this.orderMapper.toResponseDto(updatedOrder);
    }

    @KafkaListener(topics = "remove-line-item-command",
            properties = "spring.json.value.default.type=solutions.skydev.pos.order_service.model.dto.request.LineItemRequestDto")
    @SendTo("order.updated")
    @KafkaAsyncOperationBinding
    public OrderResponseDto removeLineItemCommand(LineItemRequestDto lineItemRequestDto) {
        Order updatedOrder = lineItemService.removeLineItemById(lineItemRequestDto.getId());
        return orderMapper.toResponseDto(updatedOrder);
    }
}
