package solutions.skydev.pos.order_orchestrator_service.service;

import org.springframework.stereotype.Service;
import solutions.skydev.pos.common.order_service.dto.request.LineItemRequestDto;
import solutions.skydev.pos.common.order_service.dto.request.OrderRequestDto;
import solutions.skydev.pos.order_orchestrator_service.model.entity.OrderTransaction;

import java.util.List;

@Service
public interface OrderTransactionService {
    OrderTransaction createOrderTransaction(OrderRequestDto orderRequestDto);
    OrderTransaction updateLineItem(LineItemRequestDto requestLineItem);
    OrderTransaction addLineItem(LineItemRequestDto requestLineItem);
    OrderTransaction removeLineItem(LineItemRequestDto requestLineItem);
    OrderTransaction getOrderTransactionById(Long id);
    OrderTransaction clearLineItems(OrderRequestDto orderRequestDto);
//    OrderTransaction tagDiscount(DiscountOrderRequestDto discountOrderRequestDto);
    List<OrderTransaction> getAllOrderTransactions();
    List<OrderTransaction> getOrderTransactionsByOrderId(Long orderId);
}
