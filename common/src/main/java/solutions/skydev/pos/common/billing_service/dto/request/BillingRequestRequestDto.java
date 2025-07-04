package solutions.skydev.pos.common.billing_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;


@NoArgsConstructor
@Setter
@Getter
@Builder
@AllArgsConstructor
public class BillingRequestRequestDto implements Serializable {
    Long id;
    
    @JsonProperty("net_amount")
    BigDecimal netAmount;
    
    @JsonProperty("paid_amount")
    BigDecimal paidAmount;
    
    @JsonProperty("order_id")
    Long orderId;
}