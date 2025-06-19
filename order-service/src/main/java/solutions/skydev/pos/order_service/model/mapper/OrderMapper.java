package solutions.skydev.pos.order_service.model.mapper;

import org.mapstruct.*;
import solutions.skydev.pos.order_service.model.dto.response.LineItemResponseDto;
import solutions.skydev.pos.order_service.model.dto.response.OrderResponseDto;
import solutions.skydev.pos.order_service.model.dto.response.OrderReportResponseDto;
import solutions.skydev.pos.order_service.model.dto.response.OrderResponseDto;
import solutions.skydev.pos.order_service.model.entity.Order;
import solutions.skydev.pos.order_service.model.dto.request.OrderRequestDto;

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
