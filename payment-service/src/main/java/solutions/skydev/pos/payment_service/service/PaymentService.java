package solutions.skydev.pos.payment_service.service;

import solutions.skydev.pos.payment_service.model.entity.Payment;

public interface PaymentService {
    Payment processPayment(Payment payment);
    Payment getPaymentById(Long id);
    void cancelPayment(Long id);
}
