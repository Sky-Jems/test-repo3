//package solutions.skydev.pos.discount_service.producer;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
//import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
//import io.github.springwolf.core.asyncapi.annotations.AsyncPublisher;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//import solutions.skydev.pos.discount_service.model.dto.response.ApplyDiscountResponseDto;
//import solutions.skydev.pos.discount_service.model.entity.Discount;
//import solutions.skydev.pos.discount_service.model.entity.DiscountLineItem;
//import solutions.skydev.pos.discount_service.model.mapper.DiscountMapper;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class DiscountProducer {
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//    private final ObjectMapper objectMapper;
//    private final DiscountMapper discountMapper;
//
//    @Autowired
//    public DiscountProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper, DiscountMapper discountMapper) {
//        this.kafkaTemplate = kafkaTemplate;
//        this.objectMapper = objectMapper;
//        this.discountMapper = discountMapper;
//    }
//
//    @AsyncPublisher(operation = @AsyncOperation(
//            channelName = "discount.applied",
//            description = "Discount applied event"
//    ))
//    @KafkaAsyncOperationBinding
//    public void sendDiscountApplied(DiscountLineItem discountLineItem) {
//        ApplyDiscountResponseDto discountResponseDto = null;
//        String discountResponseJson = null;
//        try {
//            discountResponseDto = this.discountMapper.toDto(discountLineItem);
//            ObjectMapper objectMapper = new ObjectMapper();
//            discountResponseJson = objectMapper.writeValueAsString(discountResponseDto);
//        } catch (Exception e) {
//            this.sendDiscountAppliedFailed(discountLineItem, "Object mapper could not be converted to JSON");
//            return;
//        }
//
//        this.kafkaTemplate.send("discount.applied", discountResponseJson);
//    }
//
//    public void sendDiscountAppliedFailed(DiscountLineItem discount, String message) {
//        this.kafkaTemplate.send("discount.apply.failed", message);
//    }
//
//    @AsyncPublisher(operation = @AsyncOperation(
//            channelName = "discount.created",
//            description = "Discount created event"
//    ))
//    @KafkaAsyncOperationBinding
//    public void sendDiscountCreated(Discount discount) {
//        String discountJson = null;
//        try {
//            discountJson = objectMapper.writeValueAsString(discount);
//        } catch (Exception e) {
//            this.sendDiscountCreatedFailed(discount, "Object mapper could not be converted to JSON");
//            return;
//        }
//
//        this.kafkaTemplate.send("discount.created", discountJson);
//    }
//
//    public void sendDiscountCreatedFailed(Discount discount, String message) {
//        Map<String, String> errorDetails = new HashMap<>();
//        errorDetails.put("error", message);
//        errorDetails.put("discountId", discount.getId().toString());
//
//        try {
//            String errorJson = objectMapper.writeValueAsString(errorDetails);
//            this.kafkaTemplate.send("discount.create.failed", errorJson);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to send discount creation failure message", e);
//        }
//    }
//
//    @AsyncPublisher(operation = @AsyncOperation(
//            channelName = "discount.updated",
//            description = "Discount updated event"
//    ))
//    @KafkaAsyncOperationBinding
//    public void sendDiscountUpdated(Discount discount) {
//        String discountJson = null;
//        try {
//            discountJson = objectMapper.writeValueAsString(discount);
//        } catch (Exception e) {
//            this.sendDiscountUpdatedFailed(discount, "Object mapper could not be converted to JSON");
//            return;
//        }
//
//        this.kafkaTemplate.send("discount.updated", discountJson);
//    }
//
//    public void sendDiscountUpdatedFailed(Discount discount, String message) {
//        Map<String, String> errorDetails = new HashMap<>();
//        errorDetails.put("error", message);
//        errorDetails.put("discountId", discount.getId().toString());
//
//        try {
//            String errorJson = objectMapper.writeValueAsString(errorDetails);
//            this.kafkaTemplate.send("discount.update.failed", errorJson);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to send discount update failure message", e);
//        }
//    }
//
//    @AsyncPublisher(operation = @AsyncOperation(
//            channelName = "discount.deleted",
//            description = "Discount deleted event"
//    ))
//    @KafkaAsyncOperationBinding
//    public void sendDiscountDeleted(Long id) {
//        String discountIdJson = null;
//        try {
//            Map<String, Long> discountIdMap = new HashMap<>();
//            discountIdMap.put("discountId", id);
//            discountIdJson = objectMapper.writeValueAsString(discountIdMap);
//        } catch (Exception e) {
//            this.sendDiscountDeletedFailed(id, "Object mapper could not be converted to JSON");
//            return;
//        }
//
//        this.kafkaTemplate.send("discount.deleted", discountIdJson);
//    }
//
//    public void sendDiscountDeletedFailed(Long id, String message) {
//        Map<String, String> errorDetails = new HashMap<>();
//        errorDetails.put("error", message);
//        errorDetails.put("discountId", id.toString());
//
//        try {
//            String errorJson = objectMapper.writeValueAsString(errorDetails);
//            this.kafkaTemplate.send("discount.delete.failed", errorJson);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to send discount deletion failure message", e);
//        }
//    }
//
//}
