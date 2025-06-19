package solutions.skydev.pos.order_orchestrator_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplyDiscountResponseDto {
    Long id;
    @JsonProperty("discount_id")
    String discountId;
    @JsonProperty("order_id")
    String orderId;
    @JsonProperty("discount_amount")
    String discountAmount;
}
