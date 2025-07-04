package solutions.skydev.pos.billing_service.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.common.billing_service.dto.response.BillingRequestResponseDto;
import solutions.skydev.pos.billing_service.model.entity.BillingRequest;
import solutions.skydev.pos.billing_service.model.mapper.BillingRequestMapper;
import solutions.skydev.pos.billing_service.service.BillingRequestService;
import solutions.skydev.pos.common.billing_service.dto.request.BillingRequestRequestDto;

@Component
public class BillingRequestConsumer {
    private final BillingRequestService billingRequestService;
    private final BillingRequestMapper billingRequestMapper;
    
    public BillingRequestConsumer(BillingRequestService billingRequestService, BillingRequestMapper billingRequestMapper) {
        this.billingRequestService = billingRequestService;
        this.billingRequestMapper = billingRequestMapper;
    }
    
    @KafkaListener(topics = "get-or-create-billing-request-command")
    @SendTo("billing-request.ready")
    public BillingRequestResponseDto createBillingRequestCommand(ConsumerRecord<String, BillingRequestRequestDto> record) {
        BillingRequest billingRequest = billingRequestMapper.toEntity(record.value());
        BillingRequest createdBillingRequest = billingRequestService.getOrcreateBillingRequest(billingRequest);
        return billingRequestMapper.toResponseDto(createdBillingRequest);
    }
    
    @KafkaListener(topics = "update-billing-request-command")
    @SendTo("billing-request.updated")
    public BillingRequestResponseDto updateBillingRequestCommand(ConsumerRecord<String, BillingRequestRequestDto> record) {
        BillingRequest billingRequest = billingRequestMapper.toEntity(record.value());
        BillingRequest updatedBillingRequest = billingRequestService.updateBillingRequest(billingRequest);
        return billingRequestMapper.toResponseDto(updatedBillingRequest);
    }
}
