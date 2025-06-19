package solutions.skydev.pos.payment_service.consumer;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.payment_service.model.dto.request.PaymentRequestDto;
import solutions.skydev.pos.payment_service.model.dto.response.PaymentResponseDto;
import solutions.skydev.pos.payment_service.model.entity.Payment;
import solutions.skydev.pos.payment_service.model.mapper.PaymentMapper;
import solutions.skydev.pos.payment_service.service.PaymentService;

@Component
public class PaymentConsumer {
    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    public PaymentConsumer(PaymentService paymentService, PaymentMapper paymentMapper) {
        this.paymentService = paymentService;
        this.paymentMapper = paymentMapper;
    }

    @KafkaListener(topics = "create-payment-command", properties = {
            "spring.json.value.default.type=solutions.skydev.pos.payment_service.model.dto.request.PaymentRequestDto"
    })
    @AsyncListener(operation = @AsyncOperation(
            channelName = "create-payment-command",
            description = "Create payment command",
            payloadType = Payment.class
    ))
    @KafkaAsyncOperationBinding
    @SendTo("payment.created")
    public PaymentResponseDto createPaymentCommand(ConsumerRecord<String, PaymentRequestDto> record) {
        PaymentRequestDto paymentRequestDto = record.value();
        Payment payment = this.paymentMapper.toEntity(paymentRequestDto);
        Payment processedPayment = paymentService.processPayment(payment);
        
        return this.paymentMapper.toResponseDto(processedPayment);
    }
}
