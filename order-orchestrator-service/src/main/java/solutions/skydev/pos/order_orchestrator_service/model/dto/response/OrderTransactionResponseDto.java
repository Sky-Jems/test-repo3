package solutions.skydev.pos.order_orchestrator_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class OrderTransactionResponseDto {
    Long id;
    
    @JsonProperty("order_id")
    String orderId;
    
    @JsonProperty("order_status")
    String orderStatus;
    
    @JsonProperty("discount_status")
    String discountStatus;
    
    @JsonProperty("payment_status")
    String paymentStatus;
    
    @JsonProperty("gross_amount")
    BigDecimal grossAmount;
    
    @JsonProperty("discount_amount")
    BigDecimal discountAmount;
    
    @JsonProperty("net_amount")
    BigDecimal netAmount;
}
