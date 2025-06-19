package solutions.skydev.pos.gateway_service.producer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class OrderOrchestratorProducer {
    private final ReplyingKafkaTemplate<String, String, String> orderTransactionCreatedReplyingTemplate;
    private final ReplyingKafkaTemplate<String, String, String> orderTransactionUpdatedReplyingTemplate;

    @Autowired
    public OrderOrchestratorProducer(ReplyingKafkaTemplate<String, String, String> orderTransactionCreatedReplyingTemplate, 
                                     ReplyingKafkaTemplate<String, String, String> orderTransactionUpdatedReplyingTemplate
                                     ) {
        this.orderTransactionCreatedReplyingTemplate = orderTransactionCreatedReplyingTemplate;
        this.orderTransactionUpdatedReplyingTemplate = orderTransactionUpdatedReplyingTemplate;
    }

    public String sendCreateOrderTransactionCommand(String requestBody) {
        ProducerRecord<String, String> record = new ProducerRecord<>("create-order-transaction-command", requestBody);
        try {
            if (!this.orderTransactionCreatedReplyingTemplate.waitForAssignment(Duration.ofSeconds(10))) {
                throw new IllegalStateException("Reply container did not initialize");
            }
            RequestReplyFuture<String, String, String> future = this.orderTransactionCreatedReplyingTemplate.sendAndReceive(record);
            ConsumerRecord<String, String> response = future.get(10,
                    TimeUnit.SECONDS);
            return response.value();
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
    public String sendUpdateLineItemCommand(String requestBody) {
        ProducerRecord<String, String> record = new ProducerRecord<>("update-order-line-item-command", requestBody);
        try {
            if (!this.orderTransactionUpdatedReplyingTemplate.waitForAssignment(Duration.ofSeconds(10))) {
                throw new IllegalStateException("Reply container did not initialize");
            }
            RequestReplyFuture<String, String, String> future = this.orderTransactionUpdatedReplyingTemplate.sendAndReceive(record);
            ConsumerRecord<String, String> response = future.get(10,
                    TimeUnit.SECONDS);
            return response.value();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send update line item command", e);
        }
    }


    public String sendRemoveLineItemCommand(String requestBody) {
        ProducerRecord<String, String> record = new ProducerRecord<>("remove-order-line-item-command", requestBody);
        try {
            if (!this.orderTransactionUpdatedReplyingTemplate.waitForAssignment(Duration.ofSeconds(10))) {
                throw new IllegalStateException("Reply container did not initialize");
            }
            RequestReplyFuture<String, String, String> future = this.orderTransactionUpdatedReplyingTemplate.sendAndReceive(record);
            ConsumerRecord<String, String> response = future.get(10,
                    TimeUnit.SECONDS);
            return response.value();
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
    public String sendAddLineItemCommand(String requestBody) {
        ProducerRecord<String, String> record = new ProducerRecord<>("add-order-line-item-command", requestBody);
        try {
            if (!this.orderTransactionUpdatedReplyingTemplate.waitForAssignment(Duration.ofSeconds(10))) {
                throw new IllegalStateException("Reply container did not initialize");
            }
            RequestReplyFuture<String, String, String> future = this.orderTransactionUpdatedReplyingTemplate.sendAndReceive(record);
            ConsumerRecord<String, String> response = future.get(10,
                    TimeUnit.SECONDS);
            return response.value();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send add line item command", e);
        }
    }

    public String sendTagOrderDiscountCommand(String requestBody) {
        ProducerRecord<String, String> record = new ProducerRecord<>("tag-discount-command", requestBody);
        try {
            if (!this.orderTransactionUpdatedReplyingTemplate.waitForAssignment(Duration.ofSeconds(10))) {
                throw new IllegalStateException("Reply container did not initialize");
            }
            RequestReplyFuture<String, String, String> future = this.orderTransactionUpdatedReplyingTemplate.sendAndReceive(record);
            ConsumerRecord<String, String> response = future.get(10,
                    TimeUnit.SECONDS);
            return response.value();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send add line item command", e);
        }
    }
}
