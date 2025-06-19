package solutions.skydev.pos.payment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.skydev.pos.payment_service.model.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> { }
