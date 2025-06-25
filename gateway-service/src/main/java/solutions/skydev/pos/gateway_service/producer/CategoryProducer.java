package solutions.skydev.pos.gateway_service.producer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.common.product_service.dto.request.CategoryRequestDto;
import solutions.skydev.pos.gateway_service.config.KafkaConfig;
import solutions.skydev.pos.common.product_service.dto.response.CategoryResponseDto;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class CategoryProducer {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplateCreated;
    private final ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplateUpdated;
    private final ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplateDeleted;

    @Autowired
    public CategoryProducer(KafkaTemplate<String, Object> kafkaTemplate, KafkaConfig kafkaConfig) {
        this.kafkaTemplate = kafkaTemplate;

        this.replyingKafkaTemplateCreated = kafkaConfig.createReplyingKafkaTemplate("category.created");
        this.replyingKafkaTemplateCreated.setSharedReplyTopic(true);
        this.replyingKafkaTemplateCreated.start();

        this.replyingKafkaTemplateUpdated = kafkaConfig.createReplyingKafkaTemplate("category.updated");
        this.replyingKafkaTemplateUpdated.setSharedReplyTopic(true);
        this.replyingKafkaTemplateUpdated.start();

        this.replyingKafkaTemplateDeleted = kafkaConfig.createReplyingKafkaTemplate("category.deleted");
        this.replyingKafkaTemplateDeleted.setSharedReplyTopic(true);
        this.replyingKafkaTemplateDeleted.start();

    }

    public CategoryResponseDto sendCategoryCreateCommand(CategoryRequestDto requestBody) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.replyingKafkaTemplateCreated.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("create-category-command", requestBody);
        RequestReplyFuture<String, Object, Object> future = this.replyingKafkaTemplateCreated.sendAndReceive(record);
        ConsumerRecord<String, Object> response = future.get(10, TimeUnit.SECONDS);
        return (CategoryResponseDto) response.value();
    }

    public CategoryResponseDto sendCategoryUpdateCommand(String id, CategoryRequestDto requestBody) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.replyingKafkaTemplateUpdated.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("update-category-command", id, requestBody);
        RequestReplyFuture<String, Object, Object> future = this.replyingKafkaTemplateUpdated.sendAndReceive(record);
        ConsumerRecord<String, Object> response = future.get(10, TimeUnit.SECONDS);
        return (CategoryResponseDto) response.value();
    }

    public CategoryResponseDto sendCategoryDeleteCommand(String id) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.replyingKafkaTemplateDeleted.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("delete-category-command", id, null);
        RequestReplyFuture<String, Object, Object> future = this.replyingKafkaTemplateDeleted.sendAndReceive(record);
        ConsumerRecord<String, Object> response = future.get(10, TimeUnit.SECONDS);
        return (CategoryResponseDto) response.value();
    }

    public void sendCategoryProductCreateCommand(String requestBody) {
        // create correlation id
        this.kafkaTemplate.send("create-category-product-command", requestBody);
    }
}
