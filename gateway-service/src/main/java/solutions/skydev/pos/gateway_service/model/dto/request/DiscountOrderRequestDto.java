package solutions.skydev.pos.gateway_service.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscountOrderRequestDto {
    @JsonProperty("order_id")
    Long orderId;
    @JsonProperty("discount_id")
    Long discountId;
}
