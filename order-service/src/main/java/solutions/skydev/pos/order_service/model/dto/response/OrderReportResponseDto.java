package solutions.skydev.pos.order_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import solutions.skydev.pos.order_service.model.entity.Order;
import solutions.skydev.pos.order_service.model.entity.LineItem;

import java.io.Serializable;
import java.util.List;


/**
 DTO for {@link Order}
 */
@Value
public class OrderReportResponseDto implements Serializable {
    List<OrderResponseDto> orders;
    @JsonProperty("total_orders")
    Long totalOrders;
    @JsonProperty("amount_of_sales")
    Double amountOfSales;
}
