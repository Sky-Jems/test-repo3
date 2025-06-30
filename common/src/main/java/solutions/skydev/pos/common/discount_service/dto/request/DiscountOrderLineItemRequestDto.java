package solutions.skydev.pos.common.discount_service.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscountOrderLineItemRequestDto {
    Long id;
    @JsonProperty("line_item_id")
    Long lineItemId;
    @JsonProperty("discount_id")
    Long discountId;
    @JsonProperty("product_id")
    Long productId;
    @JsonProperty("sub_total")
    BigDecimal subTotal;
    Integer quantity;
    BigDecimal price;
}
