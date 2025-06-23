package solutions.skydev.pos.discount_service.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.github.springwolf.core.asyncapi.annotations.AsyncPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.discount_service.model.entity.DiscountProduct;
import solutions.skydev.pos.discount_service.model.mapper.DiscountProductMapper;

@Component
public class DiscountProductProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final DiscountProductMapper discountProductMapper;

    public DiscountProductProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper, DiscountProductMapper discountProductMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.discountProductMapper = discountProductMapper;
    }

    @AsyncPublisher(operation = @AsyncOperation(
            channelName = "discount-product.created",
            description = "Discount Product created event"
    ))
    @KafkaAsyncOperationBinding
    public void sendDiscountProductCreated(DiscountProduct discountProduct) {
        String discountJson = null;
        try {
            discountJson = objectMapper.writeValueAsString(discountProduct);
        } catch (Exception e) {
            this.sendDiscountProductCreateFailed(discountProduct, "Object mapper could not be converted to JSON");
            return;
        }

        this.kafkaTemplate.send("discount.created", discountJson);
    }

    public void sendDiscountProductCreateFailed(DiscountProduct discountProduct, String message) {
        this.kafkaTemplate.send("discount.create.failed", message);
    }

    @AsyncPublisher(operation = @AsyncOperation(
            channelName = "discount-product.deleted",
            description = "Discount Product deleted event"
    ))
    @KafkaAsyncOperationBinding
    public void sendDiscountProductDeleted(DiscountProduct discountProduct) {
        String discountJson = null;
        try {
            discountJson = objectMapper.writeValueAsString(discountProduct);
        } catch (Exception e) {
            this.sendDiscountProductDeleteFailed(discountProduct, "Object mapper could not be converted to JSON");
            return;
        }

        this.kafkaTemplate.send("discount.deleted", discountJson);
    }

    public void sendDiscountProductDeleteFailed(DiscountProduct discountProduct, String message) {
        this.kafkaTemplate.send("discount.delete.failed", message);
    }
}
