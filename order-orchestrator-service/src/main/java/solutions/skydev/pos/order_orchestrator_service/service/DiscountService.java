package solutions.skydev.pos.order_orchestrator_service.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.order_orchestrator_service.config.KafkaConfig;
import solutions.skydev.pos.common.discount_service.dto.request.DiscountOrderRequestDto;
import solutions.skydev.pos.common.discount_service.dto.response.DiscountOrderUpdatedResponseDto;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class DiscountService {
    private final ReplyingKafkaTemplate<String, Object, DiscountOrderUpdatedResponseDto> replyingKafkaTemplateDiscountUpdated;
//    private final ReplyingKafkaTemplate<String, Object, DiscountOrderUpdatedResponseDto> replyingKafkaTemplateDiscountOrderCreated;

    DiscountService(KafkaConfig kafkaConfig) {
        // Initialize the replying Kafka template for discount application
        this.replyingKafkaTemplateDiscountUpdated = kafkaConfig.<DiscountOrderUpdatedResponseDto>createReplyingKafkaTemplate("discount-order.updated", DiscountOrderUpdatedResponseDto.class.getName());
        this.replyingKafkaTemplateDiscountUpdated.setSharedReplyTopic(true);
        this.replyingKafkaTemplateDiscountUpdated.start();
    }

    public DiscountOrderUpdatedResponseDto applyDiscountOrder(DiscountOrderRequestDto discountOrderRequest) throws InterruptedException, ExecutionException, TimeoutException {
        if (!this.replyingKafkaTemplateDiscountUpdated.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("apply-discount-order-command", discountOrderRequest);
        RequestReplyFuture<String, Object, DiscountOrderUpdatedResponseDto> future = this.replyingKafkaTemplateDiscountUpdated.sendAndReceive(record);
        ConsumerRecord<String, DiscountOrderUpdatedResponseDto> response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null) {
            throw new IllegalStateException("No response received for discount order creation");
        }

        return response.value();
    }

    public DiscountOrderUpdatedResponseDto updateDiscountOrder(DiscountOrderRequestDto discountOrderRequest) throws InterruptedException, ExecutionException, TimeoutException {
        if (!this.replyingKafkaTemplateDiscountUpdated.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("update-discount-order-command", discountOrderRequest);
        RequestReplyFuture<String, Object, DiscountOrderUpdatedResponseDto> future = this.replyingKafkaTemplateDiscountUpdated.sendAndReceive(record);
        ConsumerRecord<String, DiscountOrderUpdatedResponseDto> response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null) {
            throw new IllegalStateException("No response received for discount order creation");
        }

        return response.value();
    }

    public DiscountOrderUpdatedResponseDto clearDiscountOrder(DiscountOrderRequestDto discountOrderRequest) throws InterruptedException, ExecutionException, TimeoutException {
        if (!this.replyingKafkaTemplateDiscountUpdated.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("delete-discount-order-command", discountOrderRequest);
        RequestReplyFuture<String, Object, DiscountOrderUpdatedResponseDto> future = this.replyingKafkaTemplateDiscountUpdated.sendAndReceive(record);
        ConsumerRecord<String, DiscountOrderUpdatedResponseDto> response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null) {
            throw new IllegalStateException("No response received for discount order creation");
        }

        return response.value();
    }

    public DiscountOrderUpdatedResponseDto removeLineItemDiscountOrder(DiscountOrderRequestDto discountOrderRequest) throws InterruptedException, ExecutionException, TimeoutException {
        if (!this.replyingKafkaTemplateDiscountUpdated.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("delete-discount-order-line-item-command", discountOrderRequest);
        RequestReplyFuture<String, Object, DiscountOrderUpdatedResponseDto> future = this.replyingKafkaTemplateDiscountUpdated.sendAndReceive(record);
        ConsumerRecord<String, DiscountOrderUpdatedResponseDto> response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null) {
            throw new IllegalStateException("No response received for discount order creation");
        }

        return response.value();
    }
}
