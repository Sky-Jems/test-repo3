package solutions.skydev.pos.discount_service.model;

import lombok.Builder;
import lombok.Data;
import solutions.skydev.pos.discount_service.model.entity.DiscountOrder;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DiscountOrderSummary {
    private Long orderId;
    private BigDecimal discountAmount;
    private Long discountId;
    private List<DiscountOrder> discountOrders;
}
