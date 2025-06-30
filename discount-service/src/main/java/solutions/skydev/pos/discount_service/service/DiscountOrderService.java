package solutions.skydev.pos.discount_service.service;
import solutions.skydev.pos.discount_service.model.entity.DiscountOrder;
import solutions.skydev.pos.discount_service.model.entity.LineItemLevelDiscountOrder;
import solutions.skydev.pos.discount_service.model.entity.Order;
import solutions.skydev.pos.discount_service.model.entity.OrderLevelDiscountOrder;

import java.util.List;

public interface DiscountOrderService {
    DiscountOrder create(OrderLevelDiscountOrder discountOrder, Order order);
    List<LineItemLevelDiscountOrder> create(List<LineItemLevelDiscountOrder> discountOrders, Long orderId);
    DiscountOrder findByOrderId(Long id);
    List<DiscountOrder> deleteByOrderId(Long id);
    List<DiscountOrder> findAllByOrderId(Long id);
    List<LineItemLevelDiscountOrder> deleteByLineItems(Long orderId, List<LineItemLevelDiscountOrder> lineItemIds);
}
