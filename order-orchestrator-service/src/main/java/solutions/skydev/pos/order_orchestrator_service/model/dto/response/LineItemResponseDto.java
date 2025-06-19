package solutions.skydev.pos.order_orchestrator_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class LineItemResponseDto {
    Long id;
    @JsonProperty("product_id")
    Long productId;
    Integer quantity;
    Double price;
    @JsonProperty("sub_total")
    BigDecimal subTotal;
    @JsonProperty("order_id")
    Long orderId;
}
