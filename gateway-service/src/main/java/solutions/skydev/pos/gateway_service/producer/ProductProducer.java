package solutions.skydev.pos.gateway_service.producer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.AggregatingReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.common.error.domain.ServiceUnavailableException;
import solutions.skydev.pos.common.error.util.ErrorFromHeader;
import solutions.skydev.pos.common.product_service.dto.request.ProductRequestDto;
import solutions.skydev.pos.common.product_service.dto.response.ProductResponseDto;
import solutions.skydev.pos.gateway_service.config.KafkaConfig;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class ProductProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final AggregatingReplyingKafkaTemplate<String, Object, Object> createTemplate;
    private final AggregatingReplyingKafkaTemplate<String, Object, Object> updateTemplate;
    private final AggregatingReplyingKafkaTemplate<String, Object, Object> deleteTemplate;

    @Autowired
    public ProductProducer(KafkaTemplate<String, Object> kafkaTemplate,
                          KafkaConfig kafkaConfig) {
        this.kafkaTemplate = kafkaTemplate;

        this.createTemplate = kafkaConfig.createAggregatingReplyingKafkaTemplate("product.created", "create-product-command-dlt");
        this.updateTemplate = kafkaConfig.createAggregatingReplyingKafkaTemplate("product.updated", "update-product-command-dlt");
        this.deleteTemplate = kafkaConfig.createAggregatingReplyingKafkaTemplate("product.deleted", "delete-product-command-dlt");
    }

    public ProductResponseDto sendProductCreateCommand(ProductRequestDto requestBody) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.createTemplate.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("create-product-command", requestBody);
        RequestReplyFuture<String, Object, Collection<ConsumerRecord<String,Object>>> future = this.createTemplate.sendAndReceive(record);
        var response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null || response.value().isEmpty()) {
            throw new ServiceUnavailableException("No response received from the service");
        }
        // get the first response from the collection
        ConsumerRecord<String, Object> responseRecord = response.value().iterator().next();
        if (responseRecord.topic().equals("create-product-command-dlt")) {
            throw ErrorFromHeader.getDomainException(responseRecord.headers());
        } else {
            return (ProductResponseDto) responseRecord.value();
        }
    }

    public ProductResponseDto sendProductUpdateCommand(ProductRequestDto requestBody) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.updateTemplate.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("update-product-command", requestBody);
        RequestReplyFuture<String, Object, Collection<ConsumerRecord<String,Object>>> future = this.updateTemplate.sendAndReceive(record);
        var response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null || response.value().isEmpty()) {
            throw new ServiceUnavailableException("No response received from the service");
        }
        // get the first response from the collection
        ConsumerRecord<String, Object> responseRecord = response.value().iterator().next();
        if (responseRecord.topic().equals("update-product-command-dlt")) {
            throw ErrorFromHeader.getDomainException(responseRecord.headers());
        } else {
            return (ProductResponseDto) responseRecord.value();
        }
    }

    public ProductResponseDto sendProductDeleteCommand(String id) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.deleteTemplate.waitForAssignment(Duration.ofSeconds(10))) {
            throw new IllegalStateException("Reply container did not initialize");
        }
        ProducerRecord<String, Object> record = new ProducerRecord<>("delete-product-command", ProductRequestDto.builder().id(Long.valueOf(id)).build());
        RequestReplyFuture<String, Object, Collection<ConsumerRecord<String,Object>>> future = this.deleteTemplate.sendAndReceive(record);
        var response = future.get(10, TimeUnit.SECONDS);
        if (response == null || response.value() == null || response.value().isEmpty()) {
            throw new ServiceUnavailableException("No response received from the service");
        }
        // get the first response from the collection
        ConsumerRecord<String, Object> responseRecord = response.value().iterator().next();
        if (responseRecord.topic().equals("delete-product-command-dlt")) {
            throw ErrorFromHeader.getDomainException(responseRecord.headers());
        } else {
            return (ProductResponseDto) responseRecord.value();
        }
    }
}
