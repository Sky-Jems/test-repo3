package solutions.skydev.pos.gateway_service.producer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.gateway_service.config.KafkaConfig;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class ProductProducer {
    // TODO make this to a factory or singleton bean
    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplateProductCreated;
    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplateProductUpdated;
    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplateProductDeleted;


    @Autowired
    public ProductProducer(KafkaTemplate<String, String> kafkaTemplate,
                          KafkaConfig kafkaConfig) {
        // Assumes topic partition offset is set by spring-kafka
        this.replyingKafkaTemplateProductCreated = kafkaConfig.createReplyingKafkaTemplate("product.created");
        this.replyingKafkaTemplateProductCreated.setSharedReplyTopic(true);
        this.replyingKafkaTemplateProductCreated.start();

        this.replyingKafkaTemplateProductUpdated = kafkaConfig.createReplyingKafkaTemplate("product.updated");
        this.replyingKafkaTemplateProductUpdated.setSharedReplyTopic(true);
        this.replyingKafkaTemplateProductUpdated.start();

        this.replyingKafkaTemplateProductDeleted = kafkaConfig.createReplyingKafkaTemplate("product.deleted");
        this.replyingKafkaTemplateProductDeleted.setSharedReplyTopic(true);
        this.replyingKafkaTemplateProductDeleted.start();
    }

    public String sendProductCreateCommand(String requestBody) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.replyingKafkaTemplateProductCreated.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, String> record = new ProducerRecord<>("create-product-command", requestBody);
        RequestReplyFuture<String, String, String> future = this.replyingKafkaTemplateProductCreated.sendAndReceive(record);
        ConsumerRecord<String, String> response = future.get(10, TimeUnit.SECONDS);
        return response.value();
    }

    public String sendProductUpdateCommand(String id, String requestBody) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.replyingKafkaTemplateProductUpdated.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, String> record = new ProducerRecord<>("update-product-command", id, requestBody);
        RequestReplyFuture<String, String, String> future = this.replyingKafkaTemplateProductUpdated.sendAndReceive(record);
        ConsumerRecord<String, String> response = future.get(10, TimeUnit.SECONDS);
        return response.value();
    }

    public String sendProductDeleteCommand(String id) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.replyingKafkaTemplateProductDeleted.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, String> record = new ProducerRecord<>("delete-product-command", id, null);
        RequestReplyFuture<String, String, String> future = this.replyingKafkaTemplateProductDeleted.sendAndReceive(record);
        ConsumerRecord<String, String> response = future.get(10, TimeUnit.SECONDS);
        return response.value();
    }
}
