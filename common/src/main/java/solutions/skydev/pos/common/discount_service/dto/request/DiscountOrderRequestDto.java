package solutions.skydev.pos.common.discount_service.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscountOrderRequestDto {

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("discount_id")
    private Long discountId;

    @JsonProperty("total_amount")
    BigDecimal totalAmount;

    @JsonProperty("line_items")
    private List<DiscountOrderLineItemRequestDto> lineItems;


}
