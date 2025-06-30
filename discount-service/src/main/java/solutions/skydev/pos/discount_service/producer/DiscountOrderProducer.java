//package solutions.skydev.pos.discount_service.producer;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
//import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
//import io.github.springwolf.core.asyncapi.annotations.AsyncPublisher;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//import solutions.skydev.pos.discount_service.model.dto.response.ApplyDiscountResponseDto;
//import solutions.skydev.pos.discount_service.model.dto.response.DiscountOrderResponseDto;
//import solutions.skydev.pos.discount_service.model.entity.DiscountOrder;
//import solutions.skydev.pos.discount_service.model.mapper.DiscountOrderMapper;
//
//@Component
//public class DiscountOrderProducer {
//    private final KafkaTemplate<String, String> kafkaTemplate;
//    private final ObjectMapper objectMapper;
//    private final DiscountOrderMapper discountOrderMapper;
//
//    public DiscountOrderProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper, DiscountOrderMapper discountOrderMapper) {
//        this.kafkaTemplate = kafkaTemplate;
//        this.objectMapper = objectMapper;
//        this.discountOrderMapper = discountOrderMapper;
//    }
//
////    @AsyncPublisher(operation = @AsyncOperation(
////            channelName = "discount-order.created",
////            description = "Discount order created event"
////    ))
////    @KafkaAsyncOperationBinding
////    public void sendDiscountOrderCreated(DiscountOrder discountOrder) {
////        DiscountOrderResponseDto discountResponseDto = null;
////        String discountResponseJson = null;
////        try {
////            discountResponseDto = this.discountOrderMapper.toDto(discountOrder);
////            ObjectMapper objectMapper = new ObjectMapper();
////            discountResponseJson = objectMapper.writeValueAsString(discountResponseDto);
////        } catch (Exception e) {
////            this.sendDiscountOrderCreatedFailed(discountOrder, "Object mapper could not be converted to JSON");
////        }
////        this.kafkaTemplate.send("discount-order.created", discountResponseJson);
////    }
//
//    public void sendDiscountOrderCreatedFailed(DiscountOrder discountOrderJson, String message) {
//        this.kafkaTemplate.send("discount-order.create.failed", message);
//    }
//
//    public void sendDiscountOrderDeleted(DiscountOrder discountOrder) {
//        DiscountOrderResponseDto discountResponseDto = null;
//        String discountResponseJson = null;
//        try {
//            discountResponseDto = this.discountOrderMapper.toDto(discountOrder);
//            ObjectMapper objectMapper = new ObjectMapper();
//            discountResponseJson = objectMapper.writeValueAsString(discountResponseDto);
//        } catch (Exception e) {
//            this.sendDiscountOrderDeletedFailed(discountOrder, "Object mapper could not be converted to JSON");
//        }
//        this.kafkaTemplate.send("discount-order.deleted", discountResponseJson);
//    }
//
//    public void sendDiscountOrderDeletedFailed(DiscountOrder discountOrder, String message) {
//        this.kafkaTemplate.send("discount-order.delete.failed", message);
//    }
//}
