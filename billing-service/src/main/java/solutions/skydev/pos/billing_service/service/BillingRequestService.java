package solutions.skydev.pos.billing_service.service;

import solutions.skydev.pos.billing_service.model.entity.BillingRequest;

import java.util.List;

public interface BillingRequestService {
    BillingRequest getOrcreateBillingRequest(BillingRequest billingRequest);
    List<BillingRequest> findAllBillingRequests();
    List<BillingRequest> findAllBillingRequestsByOrderId(Long orderId);
    BillingRequest getBillingRequestById(Long id);
    BillingRequest updateBillingRequest(BillingRequest billingRequest);
    void deleteBillingRequest(Long id);
}