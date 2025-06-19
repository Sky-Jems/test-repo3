package solutions.skydev.pos.order_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import solutions.skydev.pos.order_service.model.entity.Order;

import lombok.Value;
import solutions.skydev.pos.order_service.model.entity.Order;
import solutions.skydev.pos.order_service.model.entity.LineItem;

import java.io.Serializable;
import java.util.List;

import java.math.BigDecimal;

/**
 * DTO for {@link Order}
 */
@Value
public class OrderResponseDto implements Serializable {
    Long id;
    String customer;
    @JsonProperty("table_number")
    String tableNumber;
    BigDecimal total;
    @JsonProperty("line_items")
    LineItemResponseDto[] lineItems;
}
