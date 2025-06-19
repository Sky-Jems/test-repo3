package solutions.skydev.pos.order_service.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;
import solutions.skydev.pos.order_service.model.entity.Order;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Order}
 */
@Builder
@Jacksonized
@Data
public class OrderRequestDto implements Serializable {
    String customer;
    @JsonProperty("table_number")
    String tableNumber;
    @JsonProperty("line_items")
    List<LineItemRequestDto> lineItems;
}
