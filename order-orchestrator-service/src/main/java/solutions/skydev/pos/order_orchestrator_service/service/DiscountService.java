package solutions.skydev.pos.order_orchestrator_service.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.order_orchestrator_service.config.KafkaConfig;
import solutions.skydev.pos.order_orchestrator_service.model.dto.request.ApplyDiscountRequestDto;
import solutions.skydev.pos.order_orchestrator_service.model.dto.request.DiscountOrderRequestDto;
import solutions.skydev.pos.order_orchestrator_service.model.dto.response.ApplyDiscountResponseDto;
import solutions.skydev.pos.order_orchestrator_service.model.dto.response.DiscountOrderResponseDto;
import solutions.skydev.pos.order_orchestrator_service.model.dto.response.OrderResponseDto;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class DiscountService {
    private final ReplyingKafkaTemplate<String, Object, ApplyDiscountResponseDto> replyingKafkaTemplateDiscountApplied;
    private final ReplyingKafkaTemplate<String, Object, DiscountOrderResponseDto> replyingKafkaTemplateDiscountOrderCreated;
    
    DiscountService(KafkaConfig kafkaConfig) {
        // Initialize the replying Kafka template for discount application
        this.replyingKafkaTemplateDiscountApplied = kafkaConfig.<ApplyDiscountResponseDto>createReplyingKafkaTemplate("discount.applied", ApplyDiscountResponseDto.class.getName());
        this.replyingKafkaTemplateDiscountApplied.setSharedReplyTopic(true);
        this.replyingKafkaTemplateDiscountApplied.start();
        
        // Initialize the replying Kafka template for discount order creation
        this.replyingKafkaTemplateDiscountOrderCreated = kafkaConfig.<DiscountOrderResponseDto>createReplyingKafkaTemplate("discount-order.created", DiscountOrderResponseDto.class.getName());
        this.replyingKafkaTemplateDiscountOrderCreated.setSharedReplyTopic(true);
        this.replyingKafkaTemplateDiscountOrderCreated.start();
    }
    
    public ApplyDiscountResponseDto applyDiscount(ApplyDiscountRequestDto requestDiscount) throws InterruptedException, ExecutionException, TimeoutException {
        if (!this.replyingKafkaTemplateDiscountApplied.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("apply-discount-command", requestDiscount);
        RequestReplyFuture<String, Object, ApplyDiscountResponseDto> future = this.replyingKafkaTemplateDiscountApplied.sendAndReceive(record);
        ConsumerRecord<String, ApplyDiscountResponseDto> response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null) {
            throw new IllegalStateException("No response received for line item update");
        }

        return response.value();
    }
    
    public DiscountOrderResponseDto createDiscountOrder(DiscountOrderRequestDto discountOrderRequest) throws InterruptedException, ExecutionException, TimeoutException {
        if (!this.replyingKafkaTemplateDiscountOrderCreated.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("create-discount-order-command", discountOrderRequest);
        RequestReplyFuture<String, Object, DiscountOrderResponseDto> future = this.replyingKafkaTemplateDiscountOrderCreated.sendAndReceive(record);
        ConsumerRecord<String, DiscountOrderResponseDto> response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null) {
            throw new IllegalStateException("No response received for discount order creation");
        }

        return response.value();
    }
    
}
