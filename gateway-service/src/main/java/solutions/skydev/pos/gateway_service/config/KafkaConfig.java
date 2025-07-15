package solutions.skydev.pos.gateway_service.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.requestreply.AggregatingReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableKafka
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
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, true);
        return props;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());

        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, true);
        // Temporary
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
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
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setCommonErrorHandler(new DefaultErrorHandler(new DeadLetterPublishingRecoverer(kafkaTemplate()), new FixedBackOff(0L, 0))); // Zero delay
        factory.setConsumerFactory(consumerFactory());
        factory.setReplyTemplate(kafkaTemplate());
        return factory;
    }

    /**
     * Creates a reply container for the specified reply topic
     * @param replyTopic the topic to listen for replies
     * @return a configured message listener container
     */
    public ConcurrentMessageListenerContainer<String, Object> createRepliesContainer(String replyTopic) {
        ContainerProperties containerProperties = new ContainerProperties(replyTopic);
        containerProperties.setGroupId(this.groupId+"-reply");
        ConcurrentMessageListenerContainer<String, Object> container = new ConcurrentMessageListenerContainer<>(consumerFactory(), containerProperties);
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); // so the new group doesn't get old replies
        container.getContainerProperties().setKafkaConsumerProperties(props);
        container.setAutoStartup(true);
        return container;
    }

    /**
     * Creates a container that listens to two reply topics
     * @param topic1 the first topic to listen for replies
     * @param topic2 the second topic to listen for replies
     * @return a configured message listener container for two topics
     */
    private ConcurrentMessageListenerContainer<String, Collection<ConsumerRecord<String, Object>>> createRepliesContainers(String topic1, String topic2) {
        ContainerProperties containerProperties = new ContainerProperties(topic1, topic2);
        containerProperties.setGroupId(this.groupId+"-reply");

        // Create a consumer factory that can handle collections of ConsumerRecord
        @SuppressWarnings("unchecked")
        ConsumerFactory<String, Collection<ConsumerRecord<String, Object>>> aggregatingConsumerFactory = 
            (ConsumerFactory<String, Collection<ConsumerRecord<String, Object>>>) (ConsumerFactory<?, ?>) consumerFactory();

        ConcurrentMessageListenerContainer<String, Collection<ConsumerRecord<String, Object>>> container = 
            new ConcurrentMessageListenerContainer<>(aggregatingConsumerFactory, containerProperties);

        Properties props = new Properties();
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); // so the new group doesn't get old replies
        container.getContainerProperties().setKafkaConsumerProperties(props);
        container.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        container.setAutoStartup(true);
        return container;
    }

    /**
     * Creates an AggregatingReplyingKafkaTemplate for the specified reply topics
     * @param topic1 the first topic to listen for replies
     * @param topic2 the second topic to listen for replies
     * @return a configured AggregatingReplyingKafkaTemplate with a release strategy of 1 reply
     */
    public AggregatingReplyingKafkaTemplate<String, Object, Object> createAggregatingReplyingKafkaTemplate(String topic1, String topic2) {
        var aggr = new AggregatingReplyingKafkaTemplate<>(
            producerFactory(), 
            createRepliesContainers(topic1, topic2), 
            (col, a) -> col.size() == 1
        );
        aggr.start();
        aggr.setSharedReplyTopic(true);
        return aggr;
    }

    /**
     * Creates a ReplyingKafkaTemplate for the specified reply topic
     * @param replyTopic the topic to listen for replies
     * @return a configured ReplyingKafkaTemplate
     */
    public ReplyingKafkaTemplate<String, Object, Object> createReplyingKafkaTemplate(String replyTopic) {
        return new ReplyingKafkaTemplate<>(producerFactory(), createRepliesContainer(replyTopic));
    }

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
