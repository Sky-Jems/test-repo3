package solutions.skydev.pos.gateway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import solutions.skydev.pos.common.config.BaseKafkaConfig;

@Configuration
@EnableKafka
public class KafkaConfig extends BaseKafkaConfig {

    // TODO make a factory or another singleton bean for this
    @Bean
    public ReplyingKafkaTemplate<String, Object, Object> orderTransactionCreatedReplyingTemplate() {
        ReplyingKafkaTemplate<String, Object, Object> template = this.createReplyingKafkaTemplate("order-transaction.created");
        template.setSharedReplyTopic(true);
        template.start();
        return template;
    }
    
    @Bean
    public ReplyingKafkaTemplate<String, Object, Object> orderTransactionUpdatedReplyingTemplate() {
        ReplyingKafkaTemplate<String, Object, Object> template = this.createReplyingKafkaTemplate("order-transaction.updated");
        template.setSharedReplyTopic(true);
        template.start();
        return template;
    }

    @Bean
    public ReplyingKafkaTemplate<String, Object, Object> orderPaymentCreatedReplyingTemplate() {
        ReplyingKafkaTemplate<String, Object, Object> template = this.createReplyingKafkaTemplate("order-payment.created");
        template.setSharedReplyTopic(true);
        template.start();
        return template;
    }
}