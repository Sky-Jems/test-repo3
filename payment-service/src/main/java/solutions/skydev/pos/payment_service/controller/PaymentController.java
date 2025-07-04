package solutions.skydev.pos.payment_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import solutions.skydev.pos.common.payment_service.dto.response.PaymentResponseDto;
import solutions.skydev.pos.payment_service.model.entity.Payment;
import solutions.skydev.pos.payment_service.model.mapper.PaymentMapper;
import solutions.skydev.pos.payment_service.model.mapper.PaymentMapperImpl;
import solutions.skydev.pos.payment_service.service.PaymentService;

import java.awt.image.ImageFilter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;
    
    public PaymentController(PaymentService paymentService, PaymentMapper paymentMapper) {
        this.paymentService = paymentService;
        this.paymentMapper = paymentMapper;
    }
    
    @GetMapping
    public List<PaymentResponseDto> getAllPayments(@RequestParam("billing_request_id") Optional<String> billingRequestId) {
        // TODO: pass this logic to service layer
        if (billingRequestId.isPresent()) {
            Long id = Long.parseLong(billingRequestId.get());
            List<Payment> payments = paymentService.getPaymentsByBillingRequestId(id);
            return paymentMapper.toResponseDto(payments);
        }
        return paymentMapper.toResponseDto(paymentService.getAllPayments());
    }
}
