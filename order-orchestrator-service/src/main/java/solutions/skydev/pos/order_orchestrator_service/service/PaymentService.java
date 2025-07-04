package solutions.skydev.pos.order_orchestrator_service.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.common.payment_service.dto.request.PaymentRequestDto;
import solutions.skydev.pos.common.payment_service.dto.response.PaymentResponseDto;
import solutions.skydev.pos.order_orchestrator_service.config.KafkaConfig;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class PaymentService {
    private final ReplyingKafkaTemplate<String, Object, PaymentResponseDto> replyingKafkaTemplatePaymentCreated;

    public PaymentService(KafkaConfig kafkaConfig) {
        // Billing request created reply template
        this.replyingKafkaTemplatePaymentCreated = kafkaConfig.createReplyingKafkaTemplate("payment.created", PaymentResponseDto.class.getName());
        this.replyingKafkaTemplatePaymentCreated.setSharedReplyTopic(true);
        this.replyingKafkaTemplatePaymentCreated.start();
    } 
    
    public PaymentResponseDto processPayment(PaymentRequestDto paymentRequestDto) throws InterruptedException, ExecutionException, TimeoutException {
        if (!this.replyingKafkaTemplatePaymentCreated.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("create-payment-command", paymentRequestDto);
        RequestReplyFuture<String, Object, PaymentResponseDto> future = this.replyingKafkaTemplatePaymentCreated.sendAndReceive(record);
        ConsumerRecord<String, PaymentResponseDto> response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null) {
            throw new IllegalStateException("No response received for order creation");
        }

        return response.value();
    }
}
