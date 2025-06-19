package solutions.skydev.pos.product_service.consumer;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.product_service.model.dto.request.VariantRequestDto;
import solutions.skydev.pos.product_service.model.entity.Variant;
import solutions.skydev.pos.product_service.model.mapper.VariantMapper;
import solutions.skydev.pos.product_service.service.VariantService;


@Component
public class VariantConsumer {
    private final VariantService variantService;
    private final VariantMapper variantMapper;

    @Autowired
    public VariantConsumer(VariantService variantService, VariantMapper variantMapper) {
        this.variantService = variantService;
        this.variantMapper = variantMapper;
    }

    @KafkaListener(topics = "create-variant-command", properties = {
            "spring.json.value.default.type=solutions.skydev.pos.product_service.model.dto.request.VariantRequestDto"
    })
    @AsyncListener(operation = @AsyncOperation(
            channelName = "create-variant-command",
            description = "Create Variant Command",
            payloadType = Variant.class
    ))
    @KafkaAsyncOperationBinding
    public void createVariantCommand(ConsumerRecord<String, VariantRequestDto> record) {
        // TODO: check if creating variant without option value persists.
        VariantRequestDto variantRequestDto = record.value();
        Variant variant = this.variantMapper.toEntity(variantRequestDto);
        this.variantService.addVariant(variant);
    }

    @KafkaListener(topics = "update-variant-command", properties = {
            "spring.json.value.default.type=solutions.skydev.pos.product_service.model.dto.request.VariantRequestDto"
    })
    @AsyncListener(operation = @AsyncOperation(
            channelName = "update-variant-command",
            description = "Update Variant Command",
            payloadType = Variant.class
    ))
    @KafkaAsyncOperationBinding
    public void updateVariantCommand(ConsumerRecord<String, VariantRequestDto> record) {
        Long variantId = Long.valueOf(record.key());
        VariantRequestDto variantRequestDto = record.value();
        Variant variant = this.variantMapper.toEntity(variantRequestDto);
        this.variantService.updateVariant(variantId, variant);
    }

    @KafkaListener(topics = "delete-variant-command")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "delete-variant-command",
            description = "Delete Variant Command"
    ))
    public void deleteVariantCommand(ConsumerRecord<String, String> record) {
        Long id = Long.valueOf(record.key());
        this.variantService.deleteVariant(id);
    }
}
