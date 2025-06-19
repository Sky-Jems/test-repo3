package solutions.skydev.pos.billing_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.billing_service.model.entity.BillingRequest;

public interface BillingRequestRepository extends JpaRepository<BillingRequest, Long> {
    // Basic CRUD operations are provided by JpaRepository
}