package solutions.skydev.pos.discount_service.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.github.springwolf.core.asyncapi.annotations.AsyncPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.discount_service.model.entity.DiscountVariant;
import solutions.skydev.pos.discount_service.model.mapper.DiscountVariantMapper;

@Component
public class DiscountVariantProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final DiscountVariantMapper discountVariantMapper;

    public DiscountVariantProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper, DiscountVariantMapper discountVariantMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.discountVariantMapper = discountVariantMapper;
    }

    @AsyncPublisher(operation = @AsyncOperation(
            channelName = "discount-variant.created",
            description = "Discount Variant created event"
    ))
    @KafkaAsyncOperationBinding
    public void sendDiscountVariantCreated(DiscountVariant discountVariant) {
        String discountJson = null;
        try {
            discountJson = objectMapper.writeValueAsString(discountVariant);
        } catch (Exception e) {
            this.sendDiscountVariantCreateFailed(discountVariant, "Object mapper could not be converted to JSON");
            return;
        }

        this.kafkaTemplate.send("discount.created", discountJson);
    }

    public void sendDiscountVariantCreateFailed(DiscountVariant discountVariant, String message) {
        this.kafkaTemplate.send("discount.create.failed", message);
    }

    @AsyncPublisher(operation = @AsyncOperation(
            channelName = "discount-variant.deleted",
            description = "Discount Variant deleted event"
    ))
    @KafkaAsyncOperationBinding
    public void sendDiscountVariantDeleted(DiscountVariant discountVariant) {
        String discountJson = null;
        try {
            discountJson = objectMapper.writeValueAsString(discountVariant);
        } catch (Exception e) {
            this.sendDiscountVariantDeleteFailed(discountVariant, "Object mapper could not be converted to JSON");
            return;
        }

        this.kafkaTemplate.send("discount.deleted", discountJson);
    }

    public void sendDiscountVariantDeleteFailed(DiscountVariant discountVariant, String message) {
        this.kafkaTemplate.send("discount.delete.failed", message);
    }
}
