package solutions.skydev.pos.billing_service.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.common.payment_service.dto.request.PaymentRequestDto;
import solutions.skydev.pos.common.payment_service.dto.response.PaymentResponseDto;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class PaymentService {

    private final ReplyingKafkaTemplate<String, Object, Object> paymentCreatedReplyingKafkaTemplate; 
    
    public PaymentService(ReplyingKafkaTemplate<String, Object, Object> paymentCreatedReplyingKafkaTemplate) {
        this.paymentCreatedReplyingKafkaTemplate = paymentCreatedReplyingKafkaTemplate;
    }
    
    public PaymentResponseDto createPaymentCommand(PaymentRequestDto paymentRequestDto) throws ExecutionException, InterruptedException, TimeoutException {
        ProducerRecord<String, Object> record = new ProducerRecord<>("create-payment-command", paymentRequestDto);
        RequestReplyFuture<String, Object, Object> future = this.paymentCreatedReplyingKafkaTemplate.sendAndReceive(record);
        ConsumerRecord<String, Object> response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null) {
            throw new IllegalStateException("No response received for order creation");
        }

        return (PaymentResponseDto) response.value();    }
}
