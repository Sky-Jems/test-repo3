package solutions.skydev.pos.order_orchestrator_service.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.common.billing_service.dto.request.BillingRequestRequestDto;
import solutions.skydev.pos.common.billing_service.dto.response.BillingRequestResponseDto;
import solutions.skydev.pos.order_orchestrator_service.config.KafkaConfig;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class BillingService {
    private final ReplyingKafkaTemplate<String, Object, BillingRequestResponseDto> replyingKafkaTemplateBillingReady;
    private final ReplyingKafkaTemplate<String, Object, BillingRequestResponseDto> replyingKafkaTemplateBillingUpdated;

    public BillingService(KafkaConfig kafkaConfig) {
        this.replyingKafkaTemplateBillingReady = kafkaConfig.<BillingRequestResponseDto>createReplyingKafkaTemplate("billing-request.ready", BillingRequestResponseDto.class.getName());
        this.replyingKafkaTemplateBillingReady.setSharedReplyTopic(true);
        this.replyingKafkaTemplateBillingReady.start();
//        billing-request.updated
        this.replyingKafkaTemplateBillingUpdated = kafkaConfig.<BillingRequestResponseDto>createReplyingKafkaTemplate("billing-request.updated", BillingRequestResponseDto.class.getName());
        this.replyingKafkaTemplateBillingUpdated.setSharedReplyTopic(true);
        this.replyingKafkaTemplateBillingUpdated.start();
    }
    
    public BillingRequestResponseDto getOrCreatedBillingRequest(BillingRequestRequestDto billingRequestRequestDto) throws InterruptedException, ExecutionException, TimeoutException {
        ProducerRecord<String, Object> record = new ProducerRecord<>("get-or-create-billing-request-command", billingRequestRequestDto);
        RequestReplyFuture<String, Object, BillingRequestResponseDto> future = this.replyingKafkaTemplateBillingReady.sendAndReceive(record);
        ConsumerRecord<String, BillingRequestResponseDto> response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null) {
            throw new IllegalStateException("No response received for line item update");
        }

        return response.value();
    }
    
    public BillingRequestResponseDto updateBillingRequest(BillingRequestRequestDto billingRequestRequestDto) throws InterruptedException, ExecutionException, TimeoutException {
        ProducerRecord<String, Object> record = new ProducerRecord<>("update-billing-request-command", billingRequestRequestDto);
        RequestReplyFuture<String, Object, BillingRequestResponseDto> future = this.replyingKafkaTemplateBillingUpdated.sendAndReceive(record);
        ConsumerRecord<String, BillingRequestResponseDto> response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null) {
            throw new IllegalStateException("No response received for line item update");
        }

        return response.value();
    }
}
