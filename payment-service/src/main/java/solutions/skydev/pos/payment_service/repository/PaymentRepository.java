package solutions.skydev.pos.payment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.payment_service.model.entity.Payment;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByBillingRequestId(Long billingRequestId);
}
