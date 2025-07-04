package solutions.skydev.pos.gateway_service.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BillingProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ReplyingKafkaTemplate<String, Object, Object> orderPaymentCreatedReplyingTemplate;
    
    
    public BillingProducer(KafkaTemplate<String, Object> kafkaTemplate, ReplyingKafkaTemplate<String, Object, Object> orderPaymentCreatedReplyingTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderPaymentCreatedReplyingTemplate = orderPaymentCreatedReplyingTemplate;
    }
}
