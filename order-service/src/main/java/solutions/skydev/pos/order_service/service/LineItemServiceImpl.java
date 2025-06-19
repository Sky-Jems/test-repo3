package solutions.skydev.pos.order_service.service;

import org.antlr.v4.runtime.InterpreterRuleContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solutions.skydev.pos.order_service.model.entity.LineItem;
import solutions.skydev.pos.order_service.model.entity.Order;
import solutions.skydev.pos.order_service.repository.LineItemRepository;
import solutions.skydev.pos.order_service.repository.OrderRepository;

import java.util.List;

@Service
public class LineItemServiceImpl implements LineItemService {
    private final LineItemRepository lineItemRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public LineItemServiceImpl(LineItemRepository lineItemRepository, OrderRepository orderRepository) {
        this.lineItemRepository = lineItemRepository;
        this.orderRepository = orderRepository;
    }
    
    public List<LineItem> getAllLineItems() {
        return lineItemRepository.findAll();
    }

    // Order's line items are lazy loaded, so we need to ensure the order exists before saving a line item
    // doing transaction ensures the line item's are fetched before saving
    // this also need for calculating the total price of the order
    // Also, rolling back of order change if line item save fails
    @Transactional
    public Order saveLineItem(LineItem lineItem) {
        Order order = this.orderRepository.findById(lineItem.getOrder().getId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + lineItem.getOrder().getId()));
        lineItem.setOrder(order);
        order.getLineItems().add(lineItem);
        lineItemRepository.save(lineItem);
        return orderRepository.findById(order.getId()).orElse(null);
    }

    public LineItem getLineItemById(Long id) { return lineItemRepository.findById(id).orElse(null); }

    @Transactional
    public Order updateLineItem(LineItem lineItem) {
        // TODO: Validate that the line item and order exist
        LineItem existingLineItem = lineItemRepository.findById(lineItem.getId())
                .orElseThrow(() -> new IllegalArgumentException("Line item not found with id: " + lineItem.getId()));
        Order order = orderRepository.findById(lineItem.getOrder().getId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + lineItem.getOrder().getId()));

        // Update the existing line item with new values
        existingLineItem.setProductId(lineItem.getProductId());
        existingLineItem.setQuantity(lineItem.getQuantity());
        existingLineItem.setPrice(lineItem.getPrice());
        existingLineItem.setOrder(order);

        // Save the updated line item
        lineItemRepository.save(existingLineItem);
        return orderRepository.findById(lineItem.getOrder().getId()).orElse(null);
    }
    
    @Transactional
    public Order removeLineItemById(Long id) {
        LineItem existingLineItem = lineItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Line item not found with id: " + id));
        Order order = existingLineItem.getOrder();
        order.getLineItems().remove(existingLineItem);
        order.calculateTotal();
        orderRepository.save(order);
        return orderRepository.findById(order.getId()).orElse(null);
    }

    public void deleteLineItem(Long id) {
        lineItemRepository.deleteById(id);
    }
}
