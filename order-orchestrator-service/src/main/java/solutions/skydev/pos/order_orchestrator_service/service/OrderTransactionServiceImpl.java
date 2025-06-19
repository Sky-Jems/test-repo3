package solutions.skydev.pos.order_orchestrator_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.order_orchestrator_service.model.dto.request.ApplyDiscountRequestDto;
import solutions.skydev.pos.order_orchestrator_service.model.dto.request.DiscountOrderRequestDto;
import solutions.skydev.pos.order_orchestrator_service.model.dto.request.LineItemRequestDto;
import solutions.skydev.pos.order_orchestrator_service.model.dto.request.OrderTransactionRequestDto;
import solutions.skydev.pos.order_orchestrator_service.model.dto.response.ApplyDiscountResponseDto;
import solutions.skydev.pos.order_orchestrator_service.model.dto.response.DiscountOrderResponseDto;
import solutions.skydev.pos.order_orchestrator_service.model.dto.response.OrderResponseDto;
import solutions.skydev.pos.order_orchestrator_service.model.dto.response.OrderTransactionResponseDto;
import solutions.skydev.pos.order_orchestrator_service.model.entity.OrderTransaction;
import solutions.skydev.pos.order_orchestrator_service.repository.OrderTransactionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class OrderTransactionServiceImpl implements OrderTransactionService {
    private final OrderTransactionRepository orderTransactionRepository;
    private final DiscountService discountService;
    private final OrderService orderService;

    @Autowired
    public OrderTransactionServiceImpl(OrderTransactionRepository orderTransactionRepository, DiscountService discountService, OrderService orderService) {
        this.orderTransactionRepository = orderTransactionRepository;
        this.discountService = discountService;
        this.orderService = orderService;
    }


    private ApplyDiscountResponseDto applyDiscount(Long orderId, Double totalAmount) {
        try {
            ApplyDiscountRequestDto applyDiscountRequestDto = ApplyDiscountRequestDto.builder()
                    .orderId(orderId)
                    .totalAmount(totalAmount)
                    .build();
            ApplyDiscountResponseDto response = discountService.applyDiscount(applyDiscountRequestDto);
            System.out.println("Discount applied: " + response.getDiscountAmount());
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private OrderTransaction updateAmounts(OrderTransaction orderTransaction, BigDecimal gross, BigDecimal discountAmount) {
        orderTransaction.setGrossAmount(gross);
        orderTransaction.setDiscountAmount(discountAmount);

        BigDecimal totalNetAmount = gross.subtract(discountAmount);
        orderTransaction.setNetAmount(totalNetAmount);

        return orderTransactionRepository.save(orderTransaction);
    }

    private OrderTransaction updateOrderTransactionAmounts(OrderTransaction orderTransaction, OrderResponseDto orderResponseDto, String discountAmount) {
        orderTransaction.setOrderId(orderResponseDto.getId());
        
        return updateAmounts(orderTransaction, orderResponseDto.getTotal(), new BigDecimal(discountAmount));
    }

    public OrderTransaction createOrderTransaction(OrderTransactionRequestDto orderTransactionRequestDto)  {
        OrderTransaction orderTransaction = orderTransactionRepository.save(new OrderTransaction());
        OrderResponseDto orderResponseDto;
        try {
            orderResponseDto = orderService.fetchOrderCreated(orderTransactionRequestDto.getOrder());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Apply discount and update order transaction
        ApplyDiscountResponseDto applyDiscountResponseDto = applyDiscount(orderResponseDto.getId(), orderResponseDto.getTotal().doubleValue());
        return updateOrderTransactionAmounts(orderTransaction, orderResponseDto, applyDiscountResponseDto.getDiscountAmount());
    }

    public OrderTransaction updateLineItem(LineItemRequestDto requestLineItem)  {
        OrderResponseDto orderResponseDto;
        try {
            orderResponseDto = orderService.updateLineItemCommand(requestLineItem);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        OrderTransaction orderTransaction = orderTransactionRepository.findByOrderId(orderResponseDto.getId()).get(0);

        // Apply discount and update order transaction
        ApplyDiscountResponseDto applyDiscountResponseDto = applyDiscount(orderResponseDto.getId(), orderResponseDto.getTotal().doubleValue());
        return updateOrderTransactionAmounts(orderTransaction, orderResponseDto, applyDiscountResponseDto.getDiscountAmount());
    }

    public OrderTransaction addLineItem(LineItemRequestDto requestLineItem)  {
        OrderResponseDto orderResponseDto;
        try {
            orderResponseDto = orderService.addLineItemCommand(requestLineItem);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        OrderTransaction orderTransaction = orderTransactionRepository.findByOrderId(orderResponseDto.getId()).get(0);

        // Apply discount and update order transaction
        ApplyDiscountResponseDto applyDiscountResponseDto = applyDiscount(orderResponseDto.getId(), orderResponseDto.getTotal().doubleValue());
        return updateOrderTransactionAmounts(orderTransaction, orderResponseDto, applyDiscountResponseDto.getDiscountAmount());
    }

    public OrderTransaction removeLineItem(LineItemRequestDto requestLineItem)  {
        OrderResponseDto orderResponseDto;
        try {
            orderResponseDto = orderService.removeLineItemCommand(requestLineItem);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        OrderTransaction orderTransaction = orderTransactionRepository.findByOrderId(orderResponseDto.getId()).get(0);

        // Apply discount and update order transaction
        ApplyDiscountResponseDto applyDiscountResponseDto = applyDiscount(orderResponseDto.getId(), orderResponseDto.getTotal().doubleValue());
        return updateOrderTransactionAmounts(orderTransaction, orderResponseDto, applyDiscountResponseDto.getDiscountAmount());
    }
    
    public OrderTransaction tagDiscount(DiscountOrderRequestDto discountOrderRequestDto) {
        DiscountOrderResponseDto discountOrderResponseDto;
        try {
            discountOrderResponseDto = discountService.createDiscountOrder(discountOrderRequestDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        OrderTransaction orderTransaction = orderTransactionRepository.findByOrderId(discountOrderResponseDto.getOrderId()).get(0);

        // Apply discount and update order transaction
        ApplyDiscountResponseDto applyDiscountResponseDto = applyDiscount(discountOrderResponseDto.getOrderId(), orderTransaction.getGrossAmount().doubleValue());
        return updateAmounts(orderTransaction, orderTransaction.getGrossAmount(), new BigDecimal(applyDiscountResponseDto.getDiscountAmount()));
    }

    public OrderTransaction getOrderTransactionById(Long id) {
        return orderTransactionRepository.findById(id).orElse(null);
    }

    @Override
    public List<OrderTransaction> getAllOrderTransactions() {
        return orderTransactionRepository.findAll();
    }

    @Override
    public List<OrderTransaction> getOrderTransactionsByOrderId(Long orderId) {
        return orderTransactionRepository.findByOrderId(orderId);
    }
}
