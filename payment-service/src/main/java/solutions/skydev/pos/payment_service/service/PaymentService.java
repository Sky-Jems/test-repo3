package solutions.skydev.pos.payment_service.service;

import solutions.skydev.pos.payment_service.model.entity.Payment;

import java.util.List;

public interface PaymentService {
    Payment processPayment(Payment payment);
    Payment getPaymentById(Long id);
    List<Payment> getPaymentsByBillingRequestId(Long orderId);
    List<Payment> getAllPayments();
    void cancelPayment(Long id);
}
