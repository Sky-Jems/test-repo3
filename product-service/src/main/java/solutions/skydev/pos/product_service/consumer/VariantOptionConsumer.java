package solutions.skydev.pos.product_service.consumer;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.product_service.model.dto.request.VariantOptionRequestDto;
import solutions.skydev.pos.product_service.model.dto.request.VariantOptionRequestDtoList;
import solutions.skydev.pos.product_service.model.entity.VariantOption;
import solutions.skydev.pos.product_service.model.mapper.VariantOptionMapper;
import solutions.skydev.pos.product_service.service.VariantOptionService;

import java.util.List;

@Component
public class VariantOptionConsumer {
    
    private final VariantOptionService variantOptionService;
    private final VariantOptionMapper variantOptionMapper;

    @Autowired
    public VariantOptionConsumer(VariantOptionService variantOptionService, VariantOptionMapper variantOptionMapper) {
        this.variantOptionService = variantOptionService;
        this.variantOptionMapper = variantOptionMapper;
    }
    
    @KafkaListener(topics = "create-variant-option-command", properties = {
            "spring.json.value.default.type=solutions.skydev.pos.product_service.model.dto.request.VariantOptionRequestDtoList"
    })
    @AsyncListener(operation = @AsyncOperation(
            channelName = "create-variant-option-command",
            description = "Create Variant Option Command",
            payloadType = VariantOption.class
    ))
    @KafkaAsyncOperationBinding
    public void createVariantOptionCommand(ConsumerRecord<String, VariantOptionRequestDtoList> record) {
        VariantOptionRequestDtoList variantOptionRequestDtoList = record.value();
        List<VariantOption> variantOptions = this.variantOptionMapper.toEntityList(variantOptionRequestDtoList.getVariantOptionRequestDtoList().stream().toList());
        variantOptions = variantOptionService.createVariantOptions(variantOptions);
        variantOptionService.generateProductVariantCombinations(variantOptions);
    }

    @KafkaListener(topics = "update-variant-option-command", properties = {
            "spring.json.value.default.type=solutions.skydev.pos.product_service.model.dto.request.VariantOptionRequestDto"
    })
    @AsyncListener(operation = @AsyncOperation(
            channelName = "update-variant-option-command",
            description = "Update Variant Option Command",
            payloadType = VariantOption.class
    ))
    @KafkaAsyncOperationBinding
    public void updateVariantOptionCommand(ConsumerRecord<String, VariantOptionRequestDto> record) {
        Long variantOptionId = Long.valueOf(record.key());
        VariantOptionRequestDto variantOptionRequestDto = record.value();
        VariantOption variantOption = this.variantOptionMapper.toEntity(variantOptionRequestDto);
        variantOptionService.updateVariantOption(variantOptionId, variantOption);
    }
    
    @KafkaListener(topics = "delete-variant-option-command")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "delete-variant-option-command",
            description = "Delete Variant Option Command"
    ))
    @KafkaAsyncOperationBinding
    public void deleteVariantOptionCommand(ConsumerRecord<String, String> record) {
        String id = record.key();
        Long variantOptionId = null;
        try {
            variantOptionId = Long.valueOf(id);
        } catch (Exception e) {
            // submit event to product.deleted.error topic 
        }
        
        variantOptionService.deleteVariantOption(variantOptionId);
    }
}
