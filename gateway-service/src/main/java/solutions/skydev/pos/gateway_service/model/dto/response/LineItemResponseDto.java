package solutions.skydev.pos.gateway_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
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

//    VariantResponseDto variant;
}

