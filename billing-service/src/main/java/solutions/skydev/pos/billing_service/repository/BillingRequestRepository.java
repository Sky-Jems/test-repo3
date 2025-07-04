package solutions.skydev.pos.billing_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.billing_service.model.entity.BillingRequest;

import java.util.List;

public interface BillingRequestRepository extends JpaRepository<BillingRequest, Long> {
    List<BillingRequest> findByOrderId(Long orderId);

    List<BillingRequest> findAllByOrderId(Long orderId);
    // Basic CRUD operations are provided by JpaRepository
}