package solutions.skydev.pos.order_orchestrator_service.service;

import org.springframework.stereotype.Service;
import solutions.skydev.pos.common.billing_service.dto.request.BillingRequestRequestDto;
import solutions.skydev.pos.common.billing_service.dto.response.BillingRequestResponseDto;
import solutions.skydev.pos.common.order_orchestrator_service.dto.request.OrderPaymentRequestDto;
import solutions.skydev.pos.common.order_orchestrator_service.dto.response.OrderTransactionResponseDto;
import solutions.skydev.pos.common.payment_service.dto.request.PaymentRequestDto;
import solutions.skydev.pos.common.payment_service.dto.response.PaymentResponseDto;
import solutions.skydev.pos.order_orchestrator_service.model.entity.OrderTransaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
public class OrderPaymentService {
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final BillingService billingService;
    private final OrderTransactionService orderTransactionService;
    
    public OrderPaymentService(OrderService orderService, PaymentService paymentService, BillingService billingService, OrderTransactionService orderTransactionService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.billingService = billingService;
        this.orderTransactionService = orderTransactionService;
    }
    
    public BillingRequestResponseDto processOrderPayment(OrderPaymentRequestDto orderPaymentRequestDto) throws ExecutionException, InterruptedException, TimeoutException {
        // Step 1: Get Order Transaction by Order ID
        List<OrderTransaction> orderTransactions = orderTransactionService.getOrderTransactionsByOrderId(orderPaymentRequestDto.getOrderId());
        if (orderTransactions.isEmpty()) {
            throw new IllegalArgumentException("No order transactions found for order ID: " + orderPaymentRequestDto.getOrderId());
        }
        OrderTransaction orderTransaction = orderTransactions.get(0); // Assuming we take the first transaction for simplicity
        
        // Step 2: Create/Get Billing Request from Order
        BillingRequestRequestDto billingRequestRequestDto = BillingRequestRequestDto.builder()
                .orderId(orderTransaction.getOrderId())
                .netAmount(orderTransaction.getNetAmount())
                .build();
        BillingRequestResponseDto billingRequestResponseDto = billingService.getOrCreatedBillingRequest(billingRequestRequestDto);
        
        // Step 3: Process Payment
        PaymentRequestDto paymentRequestDto = PaymentRequestDto.builder()
                .billingRequestId(billingRequestResponseDto.getId())
                .amount(orderPaymentRequestDto.getAmount())
                .paymentMethod(orderPaymentRequestDto.getPaymentMethod())
                .notes(orderPaymentRequestDto.getNotes())
                .build();
        PaymentResponseDto paymentResponseDto = paymentService.processPayment(paymentRequestDto);
        
        // Step 4: Update Billing Request with amount paid
        BillingRequestRequestDto updateBillingRequestDto = BillingRequestRequestDto.builder()
                .id(billingRequestResponseDto.getId())
                .netAmount(orderTransaction.getNetAmount())
                .orderId(orderTransaction.getOrderId())
                .paidAmount(paymentResponseDto.getAmount().add(billingRequestResponseDto.getPaidAmount()))
                .build();
        
        BillingRequestResponseDto updatedBalanceResponseDto = billingService.updateBillingRequest(updateBillingRequestDto);

        // Step 5: check if there is still balance to pay
        if (updatedBalanceResponseDto.getRemainingAmount().compareTo(BigDecimal.ZERO) == 0) {
            // Step 6: Update Order Status to PAID
            orderTransactionService.markOrderAsPaid(orderTransaction.getOrderId());
        }
        
        return updatedBalanceResponseDto;
    }
}
