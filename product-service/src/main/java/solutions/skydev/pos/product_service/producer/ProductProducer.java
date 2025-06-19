package solutions.skydev.pos.product_service.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.github.springwolf.core.asyncapi.annotations.AsyncPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.product_service.model.entity.Product;

import java.util.Optional;

@Component
public class ProductProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProductProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @AsyncPublisher(operation = @AsyncOperation(
            channelName = "product.created",
            description = "Product created event"
    ))
    @KafkaAsyncOperationBinding
    public void sendProductCreated(Product product) {
        String productJson = null;
        try {
            productJson = objectMapper.writeValueAsString(product);
        } catch (Exception e) {
            this.sendProductCreatedFailed(product, "Object mapper could not be converted to JSON");
            return;
        }

        this.kafkaTemplate.send("product.created", productJson);
    }

    public void sendProductCreatedFailed(Product product, String message) {
        this.kafkaTemplate.send("product.create.failed", message);
    }

     @AsyncPublisher(operation = @AsyncOperation(
             channelName = "product.updated",
             description = "Product updated event"
     ))
     @KafkaAsyncOperationBinding
    public void sendProductUpdated(Product product) {
        String productJson = null;
        try {
            productJson = objectMapper.writeValueAsString(product);
        } catch (Exception e) {
            this.sendProductUpdatedFailed(product, "Object mapper could not be converted to JSON for product update");
            return;
        }

        this.kafkaTemplate.send("product.updated", productJson);
    }

    public void sendProductUpdatedFailed(Product product, String message) {
        this.kafkaTemplate.send("product.update.failed", message);
    }

    @AsyncPublisher(operation = @AsyncOperation(
            channelName = "product.deleted",
            description = "Product deleted event"
    ))
    @KafkaAsyncOperationBinding
    public void sendProductDeleted(Optional<Product> product) {
        String productJson = null;
        try {
            productJson = objectMapper.writeValueAsString(product);
        } catch (Exception e) {
            this.sendProductDeletedFailed(product, "Object mapper could not be converted to JSON for product deletion");
            return;
        }

        this.kafkaTemplate.send("product.deleted", productJson);
    }
    public void sendProductDeletedFailed(Optional<Product> product, String message) {
        this.kafkaTemplate.send("product.delete.failed", message);
    }
}
