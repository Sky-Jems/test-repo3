package solutions.skydev.pos.common.payment_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Jacksonized
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto implements Serializable {
    private Long id;
    private BigDecimal amount;
    // TODO: make enum
    @JsonProperty("payment_method")
    private String paymentMethod;
    private String notes;
    @JsonProperty("billing_request_id")
    private Long billingRequestId;
}
