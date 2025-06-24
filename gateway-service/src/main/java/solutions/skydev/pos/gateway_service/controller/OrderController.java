package solutions.skydev.pos.gateway_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

//    @PostMapping
//    public ResponseEntity<String> createOrder(@RequestBody String order) {
//        try {
//            String response = orderProducer.sendOrderCreateCommand(order);
//            return ResponseEntity.ok(response);
//        } catch (ExecutionException | InterruptedException | TimeoutException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating order: " + e.getMessage());
//        }
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<String> updateOrder(@PathVariable String id, @RequestBody String order) {
//        orderProducer.sendOrderUpdateCommand(id, order);
//        return ResponseEntity.ok("ok");
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteOrder(@PathVariable String id) {
//        orderProducer.sendOrderDeleteCommand(id);
//        return ResponseEntity.ok("ok");
//    }
}
