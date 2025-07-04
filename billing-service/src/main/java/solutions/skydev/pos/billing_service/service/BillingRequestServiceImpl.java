package solutions.skydev.pos.billing_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.billing_service.model.entity.BillingRequest;
import solutions.skydev.pos.billing_service.repository.BillingRequestRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BillingRequestServiceImpl implements BillingRequestService {
    
    private final BillingRequestRepository billingRequestRepository;
    
    @Autowired
    public BillingRequestServiceImpl(BillingRequestRepository billingRequestRepository) {
        this.billingRequestRepository = billingRequestRepository;
    }
    
    @Override
    public BillingRequest getOrcreateBillingRequest(BillingRequest billingRequest) {
        
        List<BillingRequest> billingRequests = billingRequestRepository.findByOrderId(billingRequest.getOrderId());
        if (!billingRequests.isEmpty()) {
            BillingRequest existingBillingRequest = billingRequests.getFirst(); // Return the first found billing request
            existingBillingRequest.setNetAmount(billingRequest.getNetAmount());

            return billingRequestRepository.save(existingBillingRequest);
        }
        
        // TODO: make into a builder
        BillingRequest newBillingRequest = new BillingRequest();
        newBillingRequest.setOrderId(billingRequest.getOrderId());
        newBillingRequest.setNetAmount(billingRequest.getNetAmount());
        newBillingRequest.setPaidAmount(BigDecimal.ZERO);
        
        return billingRequestRepository.save(newBillingRequest);
    }
    
    @Override
    public List<BillingRequest> findAllBillingRequests() {
        return billingRequestRepository.findAll();
    }
    
    public List<BillingRequest> findAllBillingRequestsByOrderId(Long orderId) {
        return billingRequestRepository.findAllByOrderId(orderId);
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