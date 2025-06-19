package solutions.skydev.pos.discount_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscountLineItemResponseDto {
    private Long id;
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("discount_id")
    private Long discountId;
    @JsonProperty("discount_amount")
    private double discountAmount;
}
