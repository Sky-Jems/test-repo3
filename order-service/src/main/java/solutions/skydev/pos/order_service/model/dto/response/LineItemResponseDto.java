package solutions.skydev.pos.order_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LineItemResponseDto implements Serializable {
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
