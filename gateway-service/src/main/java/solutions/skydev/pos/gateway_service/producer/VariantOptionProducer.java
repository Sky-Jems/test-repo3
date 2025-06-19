package solutions.skydev.pos.gateway_service.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class VariantOptionProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    public VariantOptionProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    public void sendVariantOptionCreateCommand(String requestBody) {
        this.kafkaTemplate.send("create-variant-option-command", requestBody);
    }

    public void sendVariantOptionUpdateCommand(String id, String requestBody) {
        this.kafkaTemplate.send("update-variant-option-command", id, requestBody);
    }
    
    public void sendVariantOptionDeleteCommand(String id) {
        this.kafkaTemplate.send("delete-variant-option-command", id, null);
    }
}
