package solutions.skydev.pos.billing_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import solutions.skydev.pos.billing_service.model.entity.BillingRequest;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link BillingRequest}
 */
@Value
public class BillingRequestResponseDto implements Serializable {
    Long id;
    
    @JsonProperty("gross_amount")
    BigDecimal grossAmount;
    
    @JsonProperty("discount_amount")
    BigDecimal discountAmount;
    
    @JsonProperty("net_amount")
    BigDecimal netAmount;
    
    @JsonProperty("order_id")
    Long orderId;
}