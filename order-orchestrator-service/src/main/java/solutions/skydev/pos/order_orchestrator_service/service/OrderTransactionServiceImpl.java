package solutions.skydev.pos.order_orchestrator_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.common.discount_service.dto.request.DiscountOrderLineItemRequestDto;
import solutions.skydev.pos.common.order_service.dto.request.LineItemRequestDto;
import solutions.skydev.pos.common.order_service.dto.request.OrderRequestDto;
import solutions.skydev.pos.common.discount_service.dto.request.DiscountOrderRequestDto;
import solutions.skydev.pos.common.discount_service.dto.response.DiscountOrderUpdatedResponseDto;
import solutions.skydev.pos.common.order_service.dto.response.OrderResponseDto;
import solutions.skydev.pos.order_orchestrator_service.model.entity.OrderTransaction;
import solutions.skydev.pos.order_orchestrator_service.model.enums.OrderStatus;
import solutions.skydev.pos.order_orchestrator_service.repository.OrderTransactionRepository;

import javax.sound.sampled.Line;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

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


    private DiscountOrderUpdatedResponseDto clearDiscountOrder(Long orderId, BigDecimal totalAmount) {
        try {
            DiscountOrderUpdatedResponseDto response = discountService.clearDiscountOrder(buildDiscountOrderRequestDto(orderId, totalAmount, null));
            System.out.println("Discount cleared: " + response.getDiscountAmount());
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private DiscountOrderUpdatedResponseDto removeLineItemDiscountOrder(Long orderId, BigDecimal totalAmount, LineItemRequestDto lineItemRequestDto) {
        try {
            DiscountOrderUpdatedResponseDto response = discountService.removeLineItemDiscountOrder(buildDiscountOrderRequestDto(orderId, totalAmount, lineItemRequestDto));
            System.out.println("Discount line item removed: " + response.getDiscountAmount());
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private DiscountOrderUpdatedResponseDto applyDiscount(Long orderId, BigDecimal totalAmount, LineItemRequestDto lineItemRequestDto) {
        try {
            DiscountOrderRequestDto discountOrderRequestDto = buildDiscountOrderRequestDto(orderId, totalAmount, lineItemRequestDto);
            DiscountOrderUpdatedResponseDto response = discountService.applyDiscountOrder(discountOrderRequestDto);
            System.out.println("Discount applied: " + response.getDiscountAmount());
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private DiscountOrderRequestDto buildDiscountOrderRequestDto(
            Long orderId,
            BigDecimal totalAmount,
            LineItemRequestDto lineItemRequestDto
    ) {
        DiscountOrderLineItemRequestDto discountLineItem =
                mapToDiscountOrderLineItem(lineItemRequestDto);

        return DiscountOrderRequestDto.builder()
                .orderId(orderId)
                .totalAmount(totalAmount)
                .lineItems(Collections.singletonList(discountLineItem))
                .build();
    }

    private DiscountOrderLineItemRequestDto mapToDiscountOrderLineItem(LineItemRequestDto dto) {
        if (dto == null) return null;

        BigDecimal price = dto.getPrice() != null
                ? BigDecimal.valueOf(dto.getPrice())
                : BigDecimal.ZERO;

        BigDecimal subTotal = price.multiply(BigDecimal.valueOf(dto.getQuantity() != null ? dto.getQuantity() : 0));

        return DiscountOrderLineItemRequestDto.builder()
                .lineItemId(dto.getId())
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .price(price)
                .subTotal(subTotal)
                .build();
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

    public OrderTransaction createOrderTransaction(OrderRequestDto orderRequestDto)  {
        OrderTransaction orderTransaction = orderTransactionRepository.save(new OrderTransaction());
        // TODO: Validate if could create order
        OrderResponseDto orderResponseDto;
        try {
            orderResponseDto = orderService.fetchOrderCreated(orderRequestDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Apply discount and update order transaction
//        ApplyDiscountResponseDto applyDiscountResponseDto = applyDiscount(orderResponseDto.getId(), orderResponseDto.getTotal().doubleValue());
        return updateOrderTransactionAmounts(orderTransaction, orderResponseDto, "0.0");
    }

    public OrderTransaction updateLineItem(LineItemRequestDto requestLineItem)  {
        OrderResponseDto orderResponseDto;
        try {
            orderResponseDto = orderService.updateLineItemCommand(requestLineItem);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        OrderTransaction orderTransaction = orderTransactionRepository.findByOrderId(orderResponseDto.getId()).get(0);

        DiscountOrderUpdatedResponseDto discountOrderUpdatedResponseDto = applyDiscount(orderResponseDto.getId(), orderResponseDto.getTotal(), requestLineItem);
        return updateOrderTransactionAmounts(orderTransaction, orderResponseDto, discountOrderUpdatedResponseDto.getDiscountAmount().toString());
    }

    public OrderTransaction addLineItem(LineItemRequestDto requestLineItem)  {
        OrderResponseDto orderResponseDto;
        try {
            orderResponseDto = orderService.addLineItemCommand(requestLineItem);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        OrderTransaction orderTransaction = orderTransactionRepository.findByOrderId(orderResponseDto.getId()).get(0);

        DiscountOrderUpdatedResponseDto discountOrderUpdatedResponseDto = applyDiscount(orderResponseDto.getId(), orderResponseDto.getTotal(), requestLineItem);
        return updateOrderTransactionAmounts(orderTransaction, orderResponseDto, discountOrderUpdatedResponseDto.getDiscountAmount().toString());
    }

    public OrderTransaction removeLineItem(LineItemRequestDto requestLineItem)  {
        OrderResponseDto orderResponseDto;
        try {
            orderResponseDto = orderService.removeLineItemCommand(requestLineItem);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        OrderTransaction orderTransaction = orderTransactionRepository.findByOrderId(orderResponseDto.getId()).get(0);

        DiscountOrderUpdatedResponseDto discountOrderUpdatedResponseDto = removeLineItemDiscountOrder(orderResponseDto.getId(), orderResponseDto.getTotal(), requestLineItem);
        return updateOrderTransactionAmounts(orderTransaction, orderResponseDto, discountOrderUpdatedResponseDto.getDiscountAmount().toString());
    }

    public OrderTransaction clearLineItems(OrderRequestDto orderRequestDto) {
        OrderResponseDto orderResponseDto;
        try {
            orderResponseDto = orderService.clearLineItems(orderRequestDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        OrderTransaction orderTransaction = orderTransactionRepository.findByOrderId(orderResponseDto.getId()).get(0);

        DiscountOrderUpdatedResponseDto discountOrderUpdatedResponseDto = clearDiscountOrder(orderResponseDto.getId(), orderResponseDto.getTotal());
        return updateOrderTransactionAmounts(orderTransaction, orderResponseDto, discountOrderUpdatedResponseDto.getDiscountAmount().toString());
    }
public OrderTransaction markOrderAsPaid(Long orderId) {
        List<OrderTransaction> orderTransactions = orderTransactionRepository.findByOrderId(orderId);
        if (orderTransactions.isEmpty()) {
            throw new RuntimeException("No order transaction found for order ID: " + orderId);
        }

        OrderTransaction orderTransaction = orderTransactions.get(0);
        orderTransaction.setOrderStatus(OrderStatus.COMPLETED);

        return orderTransactionRepository.save(orderTransaction);
    }

    public OrderTransaction applyDiscountOrder(DiscountOrderRequestDto discountOrderRequestDto) {
        DiscountOrderUpdatedResponseDto discountOrderUpdatedResponseDto;
        try {
            // discount order already calculates the discount amount
            discountOrderUpdatedResponseDto = discountService.applyDiscountOrder(discountOrderRequestDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        OrderTransaction orderTransaction = orderTransactionRepository.findByOrderId(discountOrderUpdatedResponseDto.getOrderId()).get(0);

        return updateAmounts(orderTransaction, orderTransaction.getGrossAmount(), discountOrderUpdatedResponseDto.getDiscountAmount());
    }

public OrderTransaction removeOrderDiscount(DiscountOrderRequestDto discountOrderRequestDto) {
        DiscountOrderUpdatedResponseDto discountOrderUpdatedResponseDto;
        try {
            discountOrderUpdatedResponseDto = discountService.clearDiscountOrder(discountOrderRequestDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        OrderTransaction orderTransaction = orderTransactionRepository.findByOrderId(discountOrderUpdatedResponseDto.getOrderId()).get(0);
        return updateAmounts(orderTransaction, orderTransaction.getGrossAmount(), discountOrderUpdatedResponseDto.getDiscountAmount());
    }

public OrderTransaction removeLineItemDiscount(DiscountOrderRequestDto discountOrderRequestDto) {
        DiscountOrderUpdatedResponseDto discountOrderUpdatedResponseDto;
        try {
            discountOrderUpdatedResponseDto = discountService.removeLineItemDiscountOrder(discountOrderRequestDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        OrderTransaction orderTransaction = orderTransactionRepository.findByOrderId(discountOrderUpdatedResponseDto.getOrderId()).get(0);
        return updateAmounts(orderTransaction, orderTransaction.getGrossAmount(), discountOrderUpdatedResponseDto.getDiscountAmount());
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

    @Override
    public List<OrderTransaction> getOrderTransactionsByOrderStatus(String orderStatus) {
        OrderStatus orderStatusEnum = OrderStatus.valueOf(orderStatus);
        return orderTransactionRepository.findByOrderStatus(orderStatusEnum);
    }
}
