package solutions.skydev.pos.gateway_service.producer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.common.discount_service.dto.request.DiscountOrderRequestDto;
import solutions.skydev.pos.common.billing_service.dto.response.BillingRequestResponseDto;
import solutions.skydev.pos.common.order_orchestrator_service.dto.request.OrderPaymentRequestDto;
import solutions.skydev.pos.common.order_orchestrator_service.dto.response.OrderTransactionResponseDto;
import solutions.skydev.pos.common.order_service.dto.request.LineItemRequestDto;
import solutions.skydev.pos.common.order_service.dto.request.OrderRequestDto;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class OrderOrchestratorProducer {
    private final ReplyingKafkaTemplate<String, Object, Object> orderTransactionCreatedReplyingTemplate;
    private final ReplyingKafkaTemplate<String, Object, Object> orderTransactionUpdatedReplyingTemplate;
    private final ReplyingKafkaTemplate<String, Object, Object> orderPaymentCreatedReplyingTemplate;

    private static final Duration timeoutForInitialization = Duration.ofSeconds(1000);

    @Autowired
    public OrderOrchestratorProducer(ReplyingKafkaTemplate<String, Object, Object> orderTransactionCreatedReplyingTemplate, 
                                     ReplyingKafkaTemplate<String, Object, Object> orderTransactionUpdatedReplyingTemplate,
                                     ReplyingKafkaTemplate<String, Object, Object> orderPaymentCreatedReplyingTemplate
                                     ) {
        this.orderTransactionCreatedReplyingTemplate = orderTransactionCreatedReplyingTemplate;
        this.orderTransactionUpdatedReplyingTemplate = orderTransactionUpdatedReplyingTemplate;
        this.orderPaymentCreatedReplyingTemplate = orderPaymentCreatedReplyingTemplate;
    }

    private void waitForInitialization(ReplyingKafkaTemplate<String, Object, Object> replyingTemplate) throws InterruptedException {
        if (!replyingTemplate.waitForAssignment(OrderOrchestratorProducer.timeoutForInitialization)) {
            throw new IllegalStateException("Reply container did not initialize");
        }
    }

    public OrderTransactionResponseDto sendCreateOrderTransactionCommand(OrderRequestDto requestBody) {
        ProducerRecord<String, Object> record = new ProducerRecord<>("create-order-transaction-command", requestBody);
        try {
            waitForInitialization(orderTransactionCreatedReplyingTemplate);
            RequestReplyFuture<String, Object, Object> future = this.orderTransactionCreatedReplyingTemplate.sendAndReceive(record);
            ConsumerRecord<String, Object> response = future.get(10,
                    TimeUnit.SECONDS);
            return (OrderTransactionResponseDto) response.value();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send create order transaction command", e);
        }
    }

    /**
     * Sends a command to update a line item.
     * This method sends the request to the "update-order-line-item-command" Kafka topic
     * and waits for a response from the "order-transaction.updated" topic.
     *
     * @param requestBody JSON string containing the line item update data
     * @return Response from the order service as a JSON string
     * @throws RuntimeException if the command fails to send or no response is received
     */
    public OrderTransactionResponseDto sendUpdateLineItemCommand(LineItemRequestDto requestBody) {
        ProducerRecord<String, Object> record = new ProducerRecord<>("update-order-line-item-command", requestBody);
        try {
            waitForInitialization(orderTransactionUpdatedReplyingTemplate);
            RequestReplyFuture<String, Object, Object> future = this.orderTransactionUpdatedReplyingTemplate.sendAndReceive(record);
            ConsumerRecord<String, Object> response = future.get(10,
                    TimeUnit.SECONDS);
            return (OrderTransactionResponseDto) response.value();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send update line item command", e);
        }
    }

    /**
     * Sends a command to add a line item.
     * This method sends the request to the "add-line-item-command" Kafka topic
     * and waits for a response from the "line-item.added" topic.
     *
     * @param requestBody JSON string containing the line item data
     * @return Response from the order service as a JSON string
     * @throws RuntimeException if the command fails to send or no response is received
     */
    public OrderTransactionResponseDto sendAddLineItemCommand(LineItemRequestDto requestBody) {
        ProducerRecord<String, Object> record = new ProducerRecord<>("add-order-line-item-command", requestBody);
        try {
            waitForInitialization(orderTransactionUpdatedReplyingTemplate);
            RequestReplyFuture<String, Object, Object> future = this.orderTransactionUpdatedReplyingTemplate.sendAndReceive(record);
            ConsumerRecord<String, Object> response = future.get(10,
                    TimeUnit.SECONDS);
            return (OrderTransactionResponseDto) response.value();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send add line item command", e);
        }
    }

    public OrderTransactionResponseDto sendRemoveLineItemCommand(LineItemRequestDto requestBody) {
        ProducerRecord<String, Object> record = new ProducerRecord<>("remove-order-line-item-command", requestBody);
        try {
            waitForInitialization(orderTransactionUpdatedReplyingTemplate);
            RequestReplyFuture<String, Object, Object> future = this.orderTransactionUpdatedReplyingTemplate.sendAndReceive(record);
            ConsumerRecord<String, Object> response = future.get(10,
                    TimeUnit.SECONDS);
            return (OrderTransactionResponseDto) response.value();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send remove line item command", e);
        }
    }

    public OrderTransactionResponseDto sendClearLineItemsCommand(OrderRequestDto requestBody) {
        ProducerRecord<String, Object> record = new ProducerRecord<>("clear-order-line-items-command", requestBody);
        try {
            waitForInitialization(orderTransactionUpdatedReplyingTemplate);
            RequestReplyFuture<String, Object, Object> future = this.orderTransactionUpdatedReplyingTemplate.sendAndReceive(record);
            ConsumerRecord<String, Object> response = future.get(10,
                    TimeUnit.SECONDS);
            return (OrderTransactionResponseDto) response.value();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send clear line items command", e);
        }
    }

    public BillingRequestResponseDto sendOrderPaymentCommand(OrderPaymentRequestDto requestBody) {
        ProducerRecord<String, Object> record = new ProducerRecord<>("create-order-payment-command", requestBody);
        try {
            waitForInitialization(orderPaymentCreatedReplyingTemplate);
            RequestReplyFuture<String, Object, Object> future = this.orderPaymentCreatedReplyingTemplate.sendAndReceive(record);
            ConsumerRecord<String, Object> response = future.get(10,
                    TimeUnit.SECONDS);
            return (BillingRequestResponseDto) response.value();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send order payment command", e);
        }
    }

    public OrderTransactionResponseDto sendApplyDiscountOrderCommand(DiscountOrderRequestDto requestBody) {
        ProducerRecord<String, Object> record = new ProducerRecord<>("apply-tagged-discount-command", requestBody);
        try {
            waitForInitialization(orderTransactionUpdatedReplyingTemplate);
            RequestReplyFuture<String, Object, Object> future = this.orderTransactionUpdatedReplyingTemplate.sendAndReceive(record);
            ConsumerRecord<String, Object> response = future.get(10,
                    TimeUnit.SECONDS);
            return (OrderTransactionResponseDto) response.value();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send apply tagged discount command", e);
        }
    }

    public OrderTransactionResponseDto sendRemoveLineItemDiscount(DiscountOrderRequestDto requestBody) {
        ProducerRecord<String, Object> record = new ProducerRecord<>("delete-tagged-line-item-discount-order-command", requestBody);
        try {
            waitForInitialization(orderTransactionUpdatedReplyingTemplate);
            RequestReplyFuture<String, Object, Object> future = this.orderTransactionUpdatedReplyingTemplate.sendAndReceive(record);
            ConsumerRecord<String, Object> response = future.get(10,
                    TimeUnit.SECONDS);
            return (OrderTransactionResponseDto) response.value();
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove line items discount command", e);
        }
    }

    public OrderTransactionResponseDto sendRemoveOrderDiscount(DiscountOrderRequestDto requestBody) {
        ProducerRecord<String, Object> record = new ProducerRecord<>("delete-tagged-discount-order-command", requestBody);
        try {
            waitForInitialization(orderTransactionUpdatedReplyingTemplate);
            RequestReplyFuture<String, Object, Object> future = this.orderTransactionUpdatedReplyingTemplate.sendAndReceive(record);
            ConsumerRecord<String, Object> response = future.get(10,
                    TimeUnit.SECONDS);
            return (OrderTransactionResponseDto) response.value();
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove order discount command", e);
        }
    }
}
