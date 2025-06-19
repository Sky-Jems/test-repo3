package solutions.skydev.pos.gateway_service.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class VariantProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public VariantProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCreateVariantCommand(String requestBody) {
        this.kafkaTemplate.send("create-variant-command", requestBody);
    }

    public void sendUpdateVariantCommand(String id, String requestBody) {
        this.kafkaTemplate.send("update-variant-command", id, requestBody);
    }

    public void sendDeleteVariantCommand(String id) {
        this.kafkaTemplate.send("delete-variant-command", id, null);
    }
}
