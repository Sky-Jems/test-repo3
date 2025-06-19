package solutions.skydev.pos.order_orchestrator_service.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return props;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        // Temporary
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }

    public Map<String, Object> consumerConfigs(String defaultDeserializerClass) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, defaultDeserializerClass);
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        // Temporary
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }


    /**
     * Creates a consumer factory with the specified default deserializer class
     * @param defaultDeserializerClass the class name to use for deserialization
     * @param <V> the type of value to be deserialized
     * @return a configured consumer factory
     */
    public <V> ConsumerFactory<String, V> consumerFactory(String defaultDeserializerClass) {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(defaultDeserializerClass));
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setReplyTemplate(kafkaTemplate());
        return factory;
    }

    /**
     * Creates a reply container for the specified reply topic
     * @param replyTopic the topic to listen for replies
     * @param defaultDeserializerClass the class name to use for deserialization
     * @param <V> the type of value to be deserialized
     * @return a configured message listener container
     */
    public <V> ConcurrentMessageListenerContainer<String, V> createRepliesContainer(String replyTopic, String defaultDeserializerClass) {
        ContainerProperties containerProperties = new ContainerProperties(replyTopic);
        containerProperties.setGroupId(this.groupId+"-reply");
        ConcurrentMessageListenerContainer<String, V> container = new ConcurrentMessageListenerContainer<>(consumerFactory(defaultDeserializerClass), containerProperties);
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); // so the new group doesn't get old replies
        container.getContainerProperties().setKafkaConsumerProperties(props);
        container.setAutoStartup(true);
        return container;
    }

    /**
     * Creates a ReplyingKafkaTemplate for the specified reply topic
     * @param replyTopic the topic to listen for replies
     * @param defaultDeserializerClass the class name to use for deserialization
     * @param <V> the type of value to be deserialized
     * @return a configured ReplyingKafkaTemplate
     */
    public <V> ReplyingKafkaTemplate<String, Object, V> createReplyingKafkaTemplate(String replyTopic, String defaultDeserializerClass) {
        return new ReplyingKafkaTemplate<>(producerFactory(), createRepliesContainer(replyTopic, defaultDeserializerClass));
    }
}
