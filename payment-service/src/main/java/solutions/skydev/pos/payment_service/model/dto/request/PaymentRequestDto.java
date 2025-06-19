package solutions.skydev.pos.payment_service.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import solutions.skydev.pos.payment_service.model.entity.Payment;

import java.io.Serializable;

/**
 * DTO for {@link Payment}
 */
@Builder
@Jacksonized
@Data
public class PaymentRequestDto implements Serializable {
    @JsonProperty("billing_request_id")
    Long billingRequestId;
    Double amount;
    @JsonProperty("payment_method")
    String paymentMethod;
    String status;
}
