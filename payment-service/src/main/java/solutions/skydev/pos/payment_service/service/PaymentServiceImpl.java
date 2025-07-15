package solutions.skydev.pos.payment_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.payment_service.model.entity.Payment;
import solutions.skydev.pos.payment_service.error.domain.NegativePaymentException;
import solutions.skydev.pos.payment_service.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment processPayment(Payment payment) {
        // Proper validation of payment amount
//        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) < 0) {
//            throw new IllegalArgumentException("Payment amount cannot be negative or null.");
//        }
        
        // Testing
        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativePaymentException();
        }
        return paymentRepository.save(payment);
    }

    public Payment getPaymentById(Long id) { return paymentRepository.findById(id).orElse(null); }
    
    public List<Payment> getPaymentsByBillingRequestId(Long orderId) {
        return paymentRepository.findAllByBillingRequestId(orderId);
    }
    
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public void cancelPayment(Long id) {
        Payment payment = paymentRepository.findById(id).orElse(null);
        if (payment != null) {
            payment.setStatus("CANCELED");
            paymentRepository.save(payment);
        }
    }
}
