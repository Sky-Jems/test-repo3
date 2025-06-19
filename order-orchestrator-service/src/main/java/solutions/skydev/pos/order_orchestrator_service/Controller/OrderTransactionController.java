package solutions.skydev.pos.order_orchestrator_service.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import solutions.skydev.pos.order_orchestrator_service.model.dto.response.OrderTransactionResponseDto;
import solutions.skydev.pos.order_orchestrator_service.model.entity.OrderTransaction;
import solutions.skydev.pos.order_orchestrator_service.model.mapper.OrderTransactionMapper;
import solutions.skydev.pos.order_orchestrator_service.service.OrderTransactionService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order-transactions")
public class OrderTransactionController {
    private final OrderTransactionService orderTransactionService;
    private final OrderTransactionMapper orderTransactionMapper;

    @Autowired public OrderTransactionController(OrderTransactionService orderTransactionService, OrderTransactionMapper orderTransactionMapper) {
        this.orderTransactionService = orderTransactionService;
        this.orderTransactionMapper = orderTransactionMapper;
    }

    @GetMapping("/{id}")
    public OrderTransactionResponseDto getOrderTransactionById(@PathVariable Long id) {
        OrderTransaction orderTransaction = orderTransactionService.getOrderTransactionById(id);
        return orderTransactionMapper.toResponseDto(orderTransaction);
    }

    /**
     * Get all order transactions with optional filtering by order_id.
     * If order_id is provided, returns only transactions for that order.
     * Otherwise, returns all transactions.
     *
     * @param orderId Optional order ID to filter by
     * @return List of order transaction response DTOs
     */
    @GetMapping
    public List<OrderTransactionResponseDto> getOrderTransactions(@RequestParam(name = "order_id", required = false) Long orderId) {
        List<OrderTransaction> transactions;
        // TODO: this should happen in the service layer

        if (orderId != null) {
            // Filter by order_id if provided
            transactions = orderTransactionService.getOrderTransactionsByOrderId(orderId);
        } else {
            // Otherwise, get all transactions
            transactions = orderTransactionService.getAllOrderTransactions();
        }

        // Map entities to DTOs
        return transactions.stream()
                .map(orderTransactionMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
