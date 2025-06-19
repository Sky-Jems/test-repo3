package solutions.skydev.pos.order_service.service;

import solutions.skydev.pos.order_service.model.entity.LineItem;
import solutions.skydev.pos.order_service.model.entity.Order;

import java.util.List;

public interface LineItemService {
    Order saveLineItem(LineItem lineItem);
    List<LineItem> getAllLineItems();
    LineItem getLineItemById(Long id);
    Order updateLineItem(LineItem lineItem);
    void deleteLineItem(Long id);
    Order removeLineItemById(Long id);
}
