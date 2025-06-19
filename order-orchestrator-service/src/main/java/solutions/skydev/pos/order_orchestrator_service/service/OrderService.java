package solutions.skydev.pos.order_orchestrator_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.order_orchestrator_service.config.KafkaConfig;
import solutions.skydev.pos.order_orchestrator_service.model.dto.request.LineItemRequestDto;
import solutions.skydev.pos.order_orchestrator_service.model.dto.response.OrderResponseDto;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class OrderService {
    private final ReplyingKafkaTemplate<String, Object, OrderResponseDto> replyingKafkaTemplateOrderCreated;
    private final ReplyingKafkaTemplate<String, Object, OrderResponseDto> replyingKafkaTemplateOrderUpdated;

    public OrderService(KafkaConfig kafkaConfig) {
        // Order created reply template
        this.replyingKafkaTemplateOrderCreated = kafkaConfig.<OrderResponseDto>createReplyingKafkaTemplate("order.created", OrderResponseDto.class.getName());
        this.replyingKafkaTemplateOrderCreated.setSharedReplyTopic(true);
        this.replyingKafkaTemplateOrderCreated.start();

        // Order updated reply template
        this.replyingKafkaTemplateOrderUpdated = kafkaConfig.<OrderResponseDto>createReplyingKafkaTemplate("order.updated", OrderResponseDto.class.getName());
        this.replyingKafkaTemplateOrderUpdated.setSharedReplyTopic(true);
        this.replyingKafkaTemplateOrderUpdated.start();
    }

    public OrderResponseDto fetchOrderCreated(Object order) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.replyingKafkaTemplateOrderCreated.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String orderJson = null;
        try {
            orderJson = objectMapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("create-order-command", order);
        RequestReplyFuture<String, Object, OrderResponseDto> future = this.replyingKafkaTemplateOrderCreated.sendAndReceive(record);
        ConsumerRecord<String, OrderResponseDto> response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null) {
            throw new IllegalStateException("No response received for order creation");
        }

        return response.value();
    }

    public OrderResponseDto updateLineItemCommand(LineItemRequestDto requestLineItem) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.replyingKafkaTemplateOrderUpdated.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("update-line-item-command", requestLineItem);
        RequestReplyFuture<String, Object, OrderResponseDto> future = this.replyingKafkaTemplateOrderUpdated.sendAndReceive(record);
        ConsumerRecord<String, OrderResponseDto> response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null) {
            throw new IllegalStateException("No response received for line item update");
        }

        return response.value();
    }

    public OrderResponseDto addLineItemCommand(LineItemRequestDto requestLineItem) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.replyingKafkaTemplateOrderUpdated.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("add-line-item-command", requestLineItem);
        RequestReplyFuture<String, Object, OrderResponseDto> future = this.replyingKafkaTemplateOrderUpdated.sendAndReceive(record);
        ConsumerRecord<String, OrderResponseDto> response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null) {
            throw new IllegalStateException("No response received for line item update");
        }

        return response.value();
    }

    public OrderResponseDto removeLineItemCommand(LineItemRequestDto requestLineItem) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.replyingKafkaTemplateOrderUpdated.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("remove-line-item-command", requestLineItem);
        RequestReplyFuture<String, Object, OrderResponseDto> future = this.replyingKafkaTemplateOrderUpdated.sendAndReceive(record);
        ConsumerRecord<String, OrderResponseDto> response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null) {
            throw new IllegalStateException("No response received for line item update");
        }

        return response.value();
    }
}