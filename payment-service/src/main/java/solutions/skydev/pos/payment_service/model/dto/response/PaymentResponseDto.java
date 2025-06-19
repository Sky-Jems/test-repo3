package solutions.skydev.pos.payment_service.model.dto.response;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class PaymentResponseDto {
    Long id;
    Long billingRequestId;
    Double amount;
    String paymentMethod;
    String status; 
}
