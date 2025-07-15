package solutions.skydev.pos.gateway_service.producer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.AggregatingReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.common.error.domain.ServiceUnavailableException;
import solutions.skydev.pos.common.error.util.ErrorFromHeader;
import solutions.skydev.pos.common.product_service.dto.request.CategoryRequestDto;
import solutions.skydev.pos.gateway_service.config.KafkaConfig;
import solutions.skydev.pos.common.product_service.dto.response.CategoryResponseDto;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class CategoryProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    private final AggregatingReplyingKafkaTemplate<String, Object, Object> createTemplate;
    private final AggregatingReplyingKafkaTemplate<String, Object, Object> updateTemplate;
    private final AggregatingReplyingKafkaTemplate<String, Object, Object> deleteTemplate;

    @Autowired
    public CategoryProducer(KafkaTemplate<String, Object> kafkaTemplate, KafkaConfig kafkaConfig) {
        this.kafkaTemplate = kafkaTemplate;

        this.createTemplate = kafkaConfig.createAggregatingReplyingKafkaTemplate("category.created", "create-category-command-dlt");
        this.updateTemplate = kafkaConfig.createAggregatingReplyingKafkaTemplate("category.updated", "update-category-command-dlt");
        this.deleteTemplate = kafkaConfig.createAggregatingReplyingKafkaTemplate("category.deleted", "delete-category-command-dlt");
    }

    public CategoryResponseDto sendCategoryCreateCommand(CategoryRequestDto requestBody) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.createTemplate.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        // TODO: refactor, Same method as ProductProducer, but with different topic and request body
        ProducerRecord<String, Object> record = new ProducerRecord<>("create-category-command", requestBody);
        RequestReplyFuture<String, Object, Collection<ConsumerRecord<String,Object>>> future = this.createTemplate.sendAndReceive(record);
        var response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null || response.value().isEmpty()) {
            throw new ServiceUnavailableException("No response received from the service");
        }
        // get the first response from the collection
        ConsumerRecord<String, Object> responseRecord = response.value().iterator().next();
        if (responseRecord.topic().equals("create-category-command-dlt")) {
            throw ErrorFromHeader.getDomainException(responseRecord.headers());
        } else {
            return (CategoryResponseDto) responseRecord.value();
        }
    }

    public CategoryResponseDto sendCategoryUpdateCommand(String id, CategoryRequestDto requestBody) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.updateTemplate.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("update-category-command", id, requestBody);
        RequestReplyFuture<String, Object, Collection<ConsumerRecord<String,Object>>> future = this.updateTemplate.sendAndReceive(record);
        var response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null || response.value().isEmpty()) {
            throw new IllegalStateException("No response received for update category command");
        }
        ConsumerRecord<String, Object> responseRecord = response.value().iterator().next();
        if (responseRecord.topic().equals("update-category-command-dlt")) {
            throw ErrorFromHeader.getDomainException(responseRecord.headers());
        } else {
            return (CategoryResponseDto) responseRecord.value();
        }
    }

    public CategoryResponseDto sendCategoryDeleteCommand(String id) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.deleteTemplate.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("delete-category-command", id, null);
        RequestReplyFuture<String, Object, Collection<ConsumerRecord<String,Object>>> future = this.deleteTemplate.sendAndReceive(record);
        var response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null || response.value().isEmpty()) {
            throw new IllegalStateException("No response received for delete category command");
        }
        ConsumerRecord<String, Object> responseRecord = response.value().iterator().next();
        if (responseRecord.topic().equals("delete-category-command-dlt")) {
            throw ErrorFromHeader.getDomainException(responseRecord.headers());
        } else {
            return (CategoryResponseDto) responseRecord.value();
        }
    }

    public void sendCategoryProductCreateCommand(String requestBody) {
        // create correlation id
        this.kafkaTemplate.send("create-category-product-command", requestBody);
    }
}
