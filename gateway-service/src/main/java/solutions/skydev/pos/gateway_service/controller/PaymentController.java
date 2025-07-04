package solutions.skydev.pos.gateway_service.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import solutions.skydev.pos.common.billing_service.dto.response.BillingRequestResponseDto;
import solutions.skydev.pos.common.order_orchestrator_service.dto.request.OrderPaymentRequestDto;
import solutions.skydev.pos.gateway_service.producer.OrderOrchestratorProducer;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    
    private final OrderOrchestratorProducer orderOrchestratorProducer;
    
    
    PaymentController(OrderOrchestratorProducer orderOrchestratorProducer) {
        this.orderOrchestratorProducer = orderOrchestratorProducer;
    }
    
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<BillingRequestResponseDto> processOrderPayment(@RequestBody OrderPaymentRequestDto orderPaymentRequestDto) {
        return Mono.just(orderOrchestratorProducer.sendOrderPaymentCommand(orderPaymentRequestDto));
    }
}
