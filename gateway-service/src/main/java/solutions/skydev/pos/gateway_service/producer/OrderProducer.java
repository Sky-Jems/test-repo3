package solutions.skydev.pos.gateway_service.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Component;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import solutions.skydev.pos.common.order_service.dto.request.OrderRequestDto;
import solutions.skydev.pos.common.order_service.dto.response.OrderResponseDto;
import solutions.skydev.pos.gateway_service.config.KafkaConfig;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class OrderProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplateUpdated;

    @Autowired
    public OrderProducer(KafkaTemplate<String, Object> kafkaTemplate,
                        KafkaConfig kafkaConfig) {
        this.kafkaTemplate = kafkaTemplate;

        this.replyingKafkaTemplateUpdated = kafkaConfig.createReplyingKafkaTemplate("order.updated");
        this.replyingKafkaTemplateUpdated.setSharedReplyTopic(true);
        this.replyingKafkaTemplateUpdated.start();
    }

    public OrderResponseDto sendOrderUpdateCommand(String id, OrderRequestDto requestBody) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.replyingKafkaTemplateUpdated.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("update-customer-command", id, requestBody);
        RequestReplyFuture<String, Object, Object> future = this.replyingKafkaTemplateUpdated.sendAndReceive(record);
        ConsumerRecord<String, Object> response = future.get(10, TimeUnit.SECONDS);
        return (OrderResponseDto) response.value();
    }
}
