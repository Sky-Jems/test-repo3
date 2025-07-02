package solutions.skydev.pos.order_service.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.order_service.model.entity.LineItem;
import solutions.skydev.pos.order_service.model.entity.Order;
import solutions.skydev.pos.order_service.repository.LineItemRepository;
import solutions.skydev.pos.order_service.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final LineItemRepository lineItemRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, LineItemRepository lineItemRepository) {
        this.orderRepository = orderRepository;
        this.lineItemRepository = lineItemRepository;
    }

    @Transactional
    public Order createOrder(Order order) {
        List<LineItem> lineItems = order.getLineItems();
        order.setLineItems(new ArrayList<>());
        Order createdOrder = this.orderRepository.save(order);
        lineItems.forEach(lineItem -> lineItem.setOrder(createdOrder));
        createdOrder.getLineItems().addAll(lineItems);
        return order;
    }

    public Order getOrderById(Long id) { return orderRepository.findById(id).orElse(null); }

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        orderRepository.deleteById(id);
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Order findByOrderIdAndCreatedAtBetween(Long orderId, OffsetDateTime startDate, OffsetDateTime endDate) {
        return orderRepository.findByIdAndCreatedAtBetween(orderId, startDate, endDate);
    }
    
    @Transactional
    public Order clearLineItems(Order order) {
        Order qOrder = orderRepository.findById(order.getId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + order.getId()));
        qOrder.getLineItems().clear();
        qOrder.calculateTotal();
        return orderRepository.save(qOrder);
    }
}
