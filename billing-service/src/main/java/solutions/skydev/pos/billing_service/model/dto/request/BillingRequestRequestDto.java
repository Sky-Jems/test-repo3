package solutions.skydev.pos.billing_service.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import solutions.skydev.pos.billing_service.model.entity.BillingRequest;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link BillingRequest}
 */
@NoArgsConstructor
@Setter
@Getter
@Builder
@AllArgsConstructor
public class BillingRequestRequestDto implements Serializable {
    @JsonProperty("gross_amount")
    BigDecimal grossAmount;
    
    @JsonProperty("discount_amount")
    BigDecimal discountAmount;
    
    @JsonProperty("net_amount")
    BigDecimal netAmount;
    
    @JsonProperty("order_id")
    Long orderId;
}