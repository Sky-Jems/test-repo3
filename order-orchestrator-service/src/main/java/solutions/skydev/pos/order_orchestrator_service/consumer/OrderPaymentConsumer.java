package solutions.skydev.pos.order_orchestrator_service.consumer;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.common.billing_service.dto.response.BillingRequestResponseDto;
import solutions.skydev.pos.common.order_orchestrator_service.dto.request.OrderPaymentRequestDto;
import solutions.skydev.pos.order_orchestrator_service.service.OrderPaymentService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Component
public class OrderPaymentConsumer {
    
    private final OrderPaymentService orderPaymentService;
    
    @Autowired
    public OrderPaymentConsumer(OrderPaymentService orderPaymentService) {
        this.orderPaymentService = orderPaymentService;
    }

    @KafkaListener(topics = "create-order-payment-command")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "create-order-payment-command",
            description = "Create Order Payment Command"
    ))
    @KafkaAsyncOperationBinding
    @SendTo("order-payment.created")
    public BillingRequestResponseDto createOrderPaymentCommand(OrderPaymentRequestDto orderPaymentRequestDto) throws ExecutionException, InterruptedException, TimeoutException {
        return orderPaymentService.processOrderPayment(orderPaymentRequestDto);
    }
}
