package solutions.skydev.pos.order_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import solutions.skydev.pos.common.order_service.dto.response.OrderResponseDto;
import solutions.skydev.pos.order_service.model.entity.Order;
import solutions.skydev.pos.order_service.model.mapper.OrderMapper;
import solutions.skydev.pos.order_service.service.OrderService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrders() {
        List<Order> orders = this.orderService.findAllOrders();
        return ResponseEntity.ok(this.orderMapper.toDto(orders));
    }

    @GetMapping("/{id}")
    public OrderResponseDto getOrder(
            @PathVariable Long id,
            @RequestParam(name = "start_date", required = false) String startDate,
            @RequestParam(name = "end_date", required = false) String endDate) {
        Order order;
        if (startDate != null && endDate != null) {
            String start = isUrlEncoded(startDate) ? URLDecoder.decode(startDate, StandardCharsets.UTF_8) : startDate;
            String end = isUrlEncoded(endDate) ? URLDecoder.decode(endDate, StandardCharsets.UTF_8) : endDate;
            OffsetDateTime from = OffsetDateTime.parse(start);
            OffsetDateTime to = OffsetDateTime.parse(end);
            order = orderService.findByOrderIdAndCreatedAtBetween(id, from, to);
        } else {
            order = orderService.getOrderById(id);
        }
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        return this.orderMapper.toResponseDto(order);
    }

    private boolean isUrlEncoded(String input) {
        return input.matches(".*%[0-9A-Fa-f]{2}.*");
    }
}
