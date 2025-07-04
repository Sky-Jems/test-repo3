package solutions.skydev.pos.gateway_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import solutions.skydev.pos.common.order_service.dto.request.OrderRequestDto;
import solutions.skydev.pos.common.order_service.dto.response.OrderResponseDto;
import solutions.skydev.pos.gateway_service.producer.OrderProducer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderProducer orderProducer;

    @Autowired
    public OrderController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable String id, @RequestBody OrderRequestDto order) {
        try {
            OrderResponseDto response = this.orderProducer.sendOrderUpdateCommand(id, order);
            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
