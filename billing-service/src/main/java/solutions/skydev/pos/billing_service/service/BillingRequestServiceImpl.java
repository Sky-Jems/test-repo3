package solutions.skydev.pos.billing_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.billing_service.model.entity.BillingRequest;
import solutions.skydev.pos.billing_service.repository.BillingRequestRepository;

import java.util.List;

@Service
public class BillingRequestServiceImpl implements BillingRequestService {
    
    private final BillingRequestRepository billingRequestRepository;
    
    @Autowired
    public BillingRequestServiceImpl(BillingRequestRepository billingRequestRepository) {
        this.billingRequestRepository = billingRequestRepository;
    }
    
    @Override
    public BillingRequest createBillingRequest(BillingRequest billingRequest) {
        return billingRequestRepository.save(billingRequest);
    }
    
    @Override
    public List<BillingRequest> findAllBillingRequests() {
        return billingRequestRepository.findAll();
    }
    
    @Override
    public BillingRequest getBillingRequestById(Long id) {
        return billingRequestRepository.findById(id).orElse(null);
    }
    
    @Override
    public BillingRequest updateBillingRequest(BillingRequest billingRequest) {
        return billingRequestRepository.save(billingRequest);
    }
    
    @Override
    public void deleteBillingRequest(Long id) {
        billingRequestRepository.deleteById(id);
    }
}