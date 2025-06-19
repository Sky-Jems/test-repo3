package solutions.skydev.pos.order_orchestrator_service.model.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import solutions.skydev.pos.order_orchestrator_service.model.entity.Cart;

/**
 * DTO for {@link Cart}
 */
@Builder
@Jacksonized
@Data
public class CreateOrderRequestDto {
    Object order;
    Object payment;
}
