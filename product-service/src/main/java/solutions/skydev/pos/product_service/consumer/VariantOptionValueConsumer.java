package solutions.skydev.pos.product_service.consumer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.product_service.model.dto.request.VariantOptionValueRequestDto;
import solutions.skydev.pos.product_service.model.entity.VariantOptionValue;
import solutions.skydev.pos.product_service.model.mapper.VariantOptionValueMapper;
import solutions.skydev.pos.product_service.service.VariantOptionValueService;

@Component
public class VariantOptionValueConsumer {
    
    private final VariantOptionValueService variantOptionValueService;
    private final VariantOptionValueMapper variantOptionValueMapper;
    
    @Autowired
    public VariantOptionValueConsumer(VariantOptionValueService variantOptionValueService, VariantOptionValueMapper variantOptionValueMapper) {
        this.variantOptionValueService = variantOptionValueService;
        this.variantOptionValueMapper = variantOptionValueMapper;
    }


    @KafkaListener(topics = "create-variant-option-value-command", properties = {
            "spring.json.value.default.type=solutions.skydev.pos.product_service.model.dto.request.VariantOptionValueRequestDto"
    })
    @AsyncListener(operation = @AsyncOperation(
            channelName = "create-variant-option-value-command",
            description = "Create variant option value command"
    ))
    @KafkaAsyncOperationBinding
    public void createVariantOptionValueCommand(ConsumerRecord<String, VariantOptionValueRequestDto> record) {
        VariantOptionValueRequestDto variantOptionValueRequestDto = record.value();
        VariantOptionValue variantOptionValue = this.variantOptionValueMapper.toEntity(variantOptionValueRequestDto);
        variantOptionValueService.addVariantOptionValue(variantOptionValue);
        // submit created event with the correlation id
    }

    @KafkaListener(topics = "update-variant-option-value-command", properties = {
            "spring.json.value.default.type=solutions.skydev.pos.product_service.model.dto.request.VariantOptionValueRequestDto"
    })
    @AsyncListener(operation = @AsyncOperation(
            channelName = "update-variant-option-value-command",
            description = "Update variant option value command"
    ))
    @KafkaAsyncOperationBinding
    public void updateVariantOptionValueCommand(ConsumerRecord<String, VariantOptionValueRequestDto> record) {
        Long id = Long.valueOf(record.key());
        VariantOptionValueRequestDto variantOptionValueRequestDto = record.value();
        VariantOptionValue variantOptionValue = variantOptionValueMapper.toEntity(variantOptionValueRequestDto);
        variantOptionValueService.updateVariantOptionValue(id, variantOptionValue);
    }


    @KafkaListener(topics = "delete-variant-option-value-command")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "delete-variant-option-value-command",
            description = "Delete variant option value command"
    ))
    public void deleteVariantOptionValueCommand(ConsumerRecord<String, String> record) {
        String id = record.key();
        Long variantOptionValueId = null;
        try {
            variantOptionValueId = Long.valueOf(id);
        } catch (Exception e) {
            // submit event to product.deleted.error topic
        }
        variantOptionValueService.deleteVariantOptionValue(variantOptionValueId);
    }
}
