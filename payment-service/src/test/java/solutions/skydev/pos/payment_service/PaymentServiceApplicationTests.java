package solutions.skydev.pos.payment_service;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import solutions.skydev.pos.common.billing_service.dto.response.BillingRequestResponseDto;
import solutions.skydev.pos.common.payment_service.dto.request.PaymentRequestDto;
import solutions.skydev.pos.common.payment_service.dto.response.PaymentResponseDto;
import solutions.skydev.pos.payment_service.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@EmbeddedKafka(partitions = 1,
		topics = {"create-payment-command", "payment.created"})
class PaymentServiceApplicationTests {

	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	// Ignore intellij error
	@Autowired
	EmbeddedKafkaBroker embeddedKafkaBroker;

	private Consumer<String, PaymentResponseDto> createConsumer() {
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(groupId, "true", embeddedKafkaBroker);
		consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, PaymentResponseDto.class.getName());
		consumerProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, true);
		consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

		DefaultKafkaConsumerFactory<String, PaymentResponseDto> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
		return cf.createConsumer();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void testPaymentConsumerCreation() {
		PaymentRequestDto paymentRequestDto = PaymentRequestDto
				.builder()
				.billingRequestId(1L)
				.paymentMethod("GCASH")
				.amount(BigDecimal.valueOf(500))
				.notes("reference number: 123012975u923487")
				.build();

		kafkaTemplate.send("create-payment-command", paymentRequestDto);

		Consumer<String, PaymentResponseDto> consumer = createConsumer();
		embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);

		var record = KafkaTestUtils.getSingleRecord(consumer, "payment.created", Duration.ofSeconds(5));
		PaymentResponseDto responseDto = record.value();
		assert responseDto != null;
	}

}
