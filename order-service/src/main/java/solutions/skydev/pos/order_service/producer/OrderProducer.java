package solutions.skydev.pos.order_service.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.github.springwolf.core.asyncapi.annotations.AsyncPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.order_service.model.entity.Order;

import java.util.Optional;

@Component
public class OrderProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }
    @AsyncPublisher(operation = @AsyncOperation(
            channelName = "order.created",
            description = "Order created event"
    ))
    @KafkaAsyncOperationBinding
    public void sendOrderCreatedEvent(Order order) {
        String orderJson;
        try {
            orderJson = objectMapper.writeValueAsString(order);
        } catch (Exception e) {
            this.sendOrderCreatedFailed(order, "Object mapper could not be converted to JSON");
            return;
        }

        this.kafkaTemplate.send("order.created", orderJson);
    }

    public void sendOrderCreatedFailed(Order order, String message) {
        this.kafkaTemplate.send("order.create.failed", message);
    }

    @AsyncPublisher(operation = @AsyncOperation(
            channelName = "order.updated",
            description = "Order updated event"
    ))
    @KafkaAsyncOperationBinding
    public void sendOrderUpdatedEvent(Order order) {
        String orderJson;
        try {
            orderJson = objectMapper.writeValueAsString(order);
        } catch (Exception e) {
            this.sendOrderUpdatedFailed(order, "Object mapper could not be converted to JSON for order update");
            return;
        }

        this.kafkaTemplate.send("order.updated", orderJson);
    }

    public void sendOrderUpdatedFailed(Order order, String message) {
        this.kafkaTemplate.send("order.update.failed", message);
    }

    @AsyncPublisher(operation = @AsyncOperation(
            channelName = "order.deleted",
            description = "Order deleted event"
    ))
    @KafkaAsyncOperationBinding
    public void sendOrderDeletedEvent(Optional<Order> order) {
        String orderJson;
        try {
            orderJson = objectMapper.writeValueAsString(order);
        } catch (Exception e) {
            this.sendOrderDeletedFailed(order, "Object mapper could not be converted to JSON for order deletion");
            return;
        }

        this.kafkaTemplate.send("order.deleted", orderJson);
    }

    public void sendOrderDeletedFailed(Optional<Order> order, String message) {
        this.kafkaTemplate.send("order.delete.failed", message);
    }
}
