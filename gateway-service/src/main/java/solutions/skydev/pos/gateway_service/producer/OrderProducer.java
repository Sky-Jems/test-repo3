package solutions.skydev.pos.gateway_service.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Component;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import solutions.skydev.pos.gateway_service.config.KafkaConfig;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class OrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;
    private static final String REPLY_TOPIC = "order.created";

    @Autowired
    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate,
                        KafkaConfig kafkaConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.replyingKafkaTemplate = kafkaConfig.createReplyingKafkaTemplate(REPLY_TOPIC);
    }

    public String sendOrderCreateCommand(String requestBody) throws ExecutionException, InterruptedException, TimeoutException {
        ProducerRecord<String, String> record = new ProducerRecord<>("create-order-command", requestBody);
        RequestReplyFuture<String, String, String> future = replyingKafkaTemplate.sendAndReceive(record);
        ConsumerRecord<String, String> response = future.get(10, TimeUnit.SECONDS);
        return response.value();
    }

    public void sendOrderUpdateCommand(String id, String requestBody) {
        this.kafkaTemplate.send("update-order-command", id, requestBody);
    }

    public void sendOrderDeleteCommand(String id) {
        this.kafkaTemplate.send("delete-order-command", id, null);
    }
}
