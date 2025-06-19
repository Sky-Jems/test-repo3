package solutions.skydev.pos.billing_service.service;

import solutions.skydev.pos.billing_service.model.entity.BillingRequest;

import java.util.List;

public interface BillingRequestService {
    BillingRequest createBillingRequest(BillingRequest billingRequest);
    List<BillingRequest> findAllBillingRequests();
    BillingRequest getBillingRequestById(Long id);
    BillingRequest updateBillingRequest(BillingRequest billingRequest);
    void deleteBillingRequest(Long id);
}