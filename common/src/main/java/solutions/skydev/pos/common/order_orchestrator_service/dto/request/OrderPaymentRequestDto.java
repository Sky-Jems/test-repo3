package solutions.skydev.pos.common.order_orchestrator_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Builder
@Jacksonized
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentRequestDto {
    @JsonProperty("order_id")
    private Long orderId;
    
    private BigDecimal amount;
    @JsonProperty("payment_method")
    private String paymentMethod;
    private String notes; 
}
