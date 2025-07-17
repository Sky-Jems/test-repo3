package solutions.skydev.pos.billing_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import solutions.skydev.pos.common.config.BaseKafkaConfig;

@Configuration
public class KafkaConfig extends BaseKafkaConfig {

    @Bean
    public ReplyingKafkaTemplate<String, Object, Object> paymentCreatedReplyingKafkaTemplate() {
        ReplyingKafkaTemplate<String, Object, Object> template = this.createReplyingKafkaTemplate("payment.created");
        template.setSharedReplyTopic(true);
        template.start();
        return template;
    }
}