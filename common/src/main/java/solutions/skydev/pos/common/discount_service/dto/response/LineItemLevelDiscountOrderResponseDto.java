package solutions.skydev.pos.common.discount_service.dto.response;

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
public class LineItemLevelDiscountOrderResponseDto {

    private Long id;
    @JsonProperty("line_item_id")
    private Long lineItemId;
    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;
    private Long discountId;
}
