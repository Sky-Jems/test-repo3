package solutions.skydev.pos.billing_service;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import solutions.skydev.pos.common.billing_service.dto.response.BillingRequestResponseDto;
import solutions.skydev.pos.billing_service.repository.BillingRequestRepository;
import solutions.skydev.pos.common.billing_service.dto.request.BillingRequestRequestDto;
import solutions.skydev.pos.common.product_service.dto.response.ProductResponseDto;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@EmbeddedKafka(partitions = 1,
		topics = {"create-billing-request-command", "billing-request.created"})
class BillingServiceApplicationTests {
	
	@Autowired
	private BillingRequestRepository billingRequestRepository;

	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;
	
	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;
	
	// Ignore intellij error
	@Autowired
	EmbeddedKafkaBroker embeddedKafkaBroker;
	
	private Consumer<String, BillingRequestResponseDto> createConsumer() {
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(groupId, "true", embeddedKafkaBroker);
		consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, BillingRequestResponseDto.class.getName());
		consumerProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, true);
		consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

		DefaultKafkaConsumerFactory<String, BillingRequestResponseDto> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
		return cf.createConsumer();
	}

	@Test
	void contextLoads() {
	}
	
	@Test
	void testBillingRequestConsumerCreation() {
		BillingRequestRequestDto requestDto = BillingRequestRequestDto
				.builder()
				.orderId(123L)
				.netAmount(BigDecimal.valueOf(200))
				.build();
		
		kafkaTemplate.send("get-or-create-billing-request-command", requestDto);
		
		Consumer<String, BillingRequestResponseDto> consumer = createConsumer();
		embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
		
		var record = KafkaTestUtils.getSingleRecord(consumer, "billing-request.get-or-created", Duration.ofSeconds(5));
		BillingRequestResponseDto responseDto = record.value();
		assert responseDto != null;
	}

}
