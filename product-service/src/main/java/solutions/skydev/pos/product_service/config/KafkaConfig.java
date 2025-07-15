package solutions.skydev.pos.product_service.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ConsumerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;
import solutions.skydev.pos.common.error.domain.DomainException;
import solutions.skydev.pos.common.product_service.dto.request.ProductRequestDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, true);
        // Temporary
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    } 
    
    // Default Consumer Factory
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }
    
    
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    // Concurrent Listener container factory
    @Bean
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

    // Standard KafkaTemplate
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
