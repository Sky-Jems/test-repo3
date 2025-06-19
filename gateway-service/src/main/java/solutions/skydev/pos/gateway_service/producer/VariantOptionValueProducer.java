package solutions.skydev.pos.gateway_service.producer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class VariantOptionValueProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public VariantOptionValueProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendVariantOptionValueCreateCommand(String requestBody) {
        this.kafkaTemplate.send("create-variant-option-value-command", requestBody);
    }

    public void sendVariantOptionValueUpdateCommand(String id, String requestBody) {
        this.kafkaTemplate.send("update-variant-option-value-command", id, requestBody);
    }

    public void sendVariantOptionValueDeleteCommand(String id) {
        this.kafkaTemplate.send("delete-variant-option-value-command", id, null);
    }
}
