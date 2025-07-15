package solutions.skydev.pos.common.discount_service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DiscountOrderUpdatedResponseDto {
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;
    @JsonProperty("discount")
    private DiscountResponseDto discount;
    @JsonProperty("line_items")
    private List<LineItemLevelDiscountOrderResponseDto> lineItems;
}
