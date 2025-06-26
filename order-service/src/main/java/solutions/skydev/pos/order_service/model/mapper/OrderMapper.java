package solutions.skydev.pos.order_service.model.mapper;

import org.mapstruct.*;
import solutions.skydev.pos.common.order_service.dto.request.OrderRequestDto;
import solutions.skydev.pos.common.order_service.dto.response.LineItemResponseDto;
import solutions.skydev.pos.common.order_service.dto.response.OrderResponseDto;
import solutions.skydev.pos.order_service.model.entity.Order;

import java.util.List;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface OrderMapper {
    OrderRequestDto toDto(Order order);
    Order toEntity(OrderRequestDto orderRequestDto);
    OrderResponseDto toResponseDto(Order order);
    List<OrderResponseDto> toResponseDtoList(List<Order> order);
//    OrderReportResponseDto toResponseReportDto(List<Order> orders);
    List<OrderResponseDto> toDto(List<Order> orders);
    @Mapping(source = "orderId", target = "id")
    Order toEntity(LineItemResponseDto lineItemRequestDto);
}
