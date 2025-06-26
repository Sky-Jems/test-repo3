package solutions.skydev.pos.common.order_orchestrator_service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import solutions.skydev.pos.common.order_service.dto.response.OrderResponseDto;

import java.math.BigDecimal;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class OrderTransactionResponseDto {
    Long id;
    
    @JsonProperty("order_id")
    Long orderId;
    
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
    
    
    OrderResponseDto order;
}
