package solutions.skydev.pos.billing_service.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.billing_service.model.dto.request.BillingRequestRequestDto;
import solutions.skydev.pos.billing_service.model.dto.response.BillingRequestResponseDto;
import solutions.skydev.pos.billing_service.model.entity.BillingRequest;
import solutions.skydev.pos.billing_service.model.mapper.BillingRequestMapper;
import solutions.skydev.pos.billing_service.service.BillingRequestService;

@Component
public class BillingRequestConsumer {
    private final BillingRequestService billingRequestService;
    private final BillingRequestMapper billingRequestMapper;
    
    public BillingRequestConsumer(BillingRequestService billingRequestService, BillingRequestMapper billingRequestMapper) {
        this.billingRequestService = billingRequestService;
        this.billingRequestMapper = billingRequestMapper;
    }
    
    @KafkaListener(topics = "create-billing-request-command",
            properties = "spring.json.value.default.type=solutions.skydev.pos.billing_service.model.dto.request.BillingRequestRequestDto")
    @SendTo("billing-request.created")
    public BillingRequestResponseDto createBillingRequestCommand(ConsumerRecord<String, BillingRequestRequestDto> record) {
        BillingRequest billingRequest = billingRequestMapper.toEntity(record.value());
        BillingRequest createdBillingRequest = billingRequestService.createBillingRequest(billingRequest);
        return billingRequestMapper.toResponseDto(createdBillingRequest);
    }
}
