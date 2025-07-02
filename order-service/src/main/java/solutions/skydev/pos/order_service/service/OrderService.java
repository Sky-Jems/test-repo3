package solutions.skydev.pos.order_service.service;

import solutions.skydev.pos.order_service.model.entity.Order;

import java.time.OffsetDateTime;
import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    List<Order> findAllOrders();
    Order getOrderById(Long id);
    Order clearLineItems(Order order);
    Order updateOrder(Order order);
    void deleteOrder(Long id);
    Order findByOrderIdAndCreatedAtBetween(Long orderId, OffsetDateTime startDate, OffsetDateTime endDate);
}
