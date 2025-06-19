package solutions.skydev.pos.order_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solutions.skydev.pos.order_service.model.dto.response.OrderResponseDto;
import solutions.skydev.pos.order_service.model.entity.Order;
import solutions.skydev.pos.order_service.model.mapper.OrderMapper;
import solutions.skydev.pos.order_service.service.OrderService;

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
    public OrderResponseDto getOrder(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return this.orderMapper.toResponseDto(order);
    }

//    @GetMapping("/reports")
//    public OrderReportResponseDto getOrderReports(
//            @RequestParam(name = "start_date", required = false)
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//            OffsetDateTime startDate,
//            @RequestParam(name = "end_date", required = false)
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//            OffsetDateTime endDate
//    ) {
//        // TODO: enhance conversion of date time with timezone
//        List<Order> orders = orderService.getOrderReports(startDate, endDate);
//        return this.orderMapper.toResponseReportDto(orders);
//    }
}
