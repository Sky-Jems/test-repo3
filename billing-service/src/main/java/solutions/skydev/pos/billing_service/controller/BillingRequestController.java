package solutions.skydev.pos.billing_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.skydev.pos.billing_service.model.entity.BillingRequest;
import solutions.skydev.pos.billing_service.model.dto.request.BillingRequestRequestDto;
import solutions.skydev.pos.billing_service.model.dto.response.BillingRequestResponseDto;
import solutions.skydev.pos.billing_service.model.mapper.BillingRequestMapper;
import solutions.skydev.pos.billing_service.service.BillingRequestService;

import java.util.List;

@RestController
@RequestMapping("/billing-requests")
public class BillingRequestController {

    private final BillingRequestService billingRequestService;
    private final BillingRequestMapper billingRequestMapper;

    @Autowired
    public BillingRequestController(BillingRequestService billingRequestService, BillingRequestMapper billingRequestMapper) {
        this.billingRequestService = billingRequestService;
        this.billingRequestMapper = billingRequestMapper;
    }

    @GetMapping
    public ResponseEntity<List<BillingRequestResponseDto>> getAllBillingRequests() {
        List<BillingRequest> billingRequests = billingRequestService.findAllBillingRequests();
        return ResponseEntity.ok(billingRequestMapper.toResponseDtoList(billingRequests));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillingRequestResponseDto> getBillingRequestById(@PathVariable Long id) {
        BillingRequest billingRequest = billingRequestService.getBillingRequestById(id);
        if (billingRequest == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(billingRequestMapper.toResponseDto(billingRequest));
    }

    @PostMapping
    public ResponseEntity<BillingRequestResponseDto> createBillingRequest(@RequestBody BillingRequestRequestDto requestDto) {
        BillingRequest billingRequest = billingRequestMapper.toEntity(requestDto);
        BillingRequest createdBillingRequest = billingRequestService.createBillingRequest(billingRequest);
        return new ResponseEntity<>(billingRequestMapper.toResponseDto(createdBillingRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillingRequestResponseDto> updateBillingRequest(
            @PathVariable Long id,
            @RequestBody BillingRequestRequestDto requestDto) {
        BillingRequest existingBillingRequest = billingRequestService.getBillingRequestById(id);
        if (existingBillingRequest == null) {
            return ResponseEntity.notFound().build();
        }
        
        BillingRequest billingRequestToUpdate = billingRequestMapper.toEntity(requestDto);
        billingRequestToUpdate.setId(id);
        
        BillingRequest updatedBillingRequest = billingRequestService.updateBillingRequest(billingRequestToUpdate);
        return ResponseEntity.ok(billingRequestMapper.toResponseDto(updatedBillingRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBillingRequest(@PathVariable Long id) {
        BillingRequest existingBillingRequest = billingRequestService.getBillingRequestById(id);
        if (existingBillingRequest == null) {
            return ResponseEntity.notFound().build();
        }
        
        billingRequestService.deleteBillingRequest(id);
        return ResponseEntity.noContent().build();
    }
}