package solutions.skydev.pos.common.billing_service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class BillingRequestResponseDto implements Serializable {
    Long id;
    
    @JsonProperty("net_amount")
    BigDecimal netAmount;
    
    @JsonProperty("order_id")
    Long orderId;
    
    @JsonProperty("paid_amount")
    BigDecimal paidAmount;
    
    @JsonProperty("remaining_amount")
    BigDecimal remainingAmount;
}