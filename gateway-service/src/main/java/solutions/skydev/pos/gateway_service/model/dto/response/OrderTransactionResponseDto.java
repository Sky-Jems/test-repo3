package solutions.skydev.pos.gateway_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
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
