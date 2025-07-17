package solutions.skydev.pos.product_service.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
import solutions.skydev.pos.common.config.BaseKafkaConfig;
import solutions.skydev.pos.common.error.domain.DomainException;

@Configuration
public class KafkaConfig extends BaseKafkaConfig {

    @Bean
    @Override
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate());
        recoverer.setExceptionHeadersCreator((kafkaHeaders, exception, isKey, headerNames) -> {
            if (exception.getCause() != null && exception.getCause() instanceof DomainException domainException) {
                kafkaHeaders.add(new RecordHeader("exception_error_code", domainException.getErrorCode().getCode().getBytes()));
                kafkaHeaders.add(new RecordHeader("exception_error_message", domainException.getMessage().getBytes()));
                kafkaHeaders.add(new RecordHeader("exception_error_http_status_code", String.valueOf(domainException.getHttpStatus().value()).getBytes()));
            }
        });
        
        factory.setCommonErrorHandler(new DefaultErrorHandler(recoverer, new FixedBackOff(0L, 0))); // Zero delay
        factory.setConsumerFactory(consumerFactory());
        factory.setReplyTemplate(kafkaTemplate());
        factory.setReplyHeadersConfigurer((headerName, headerValue) -> {
            return headerName.equals("kafka_correlationId"); // Copy the reply topic header
        });
        return factory;
    }
}