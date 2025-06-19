package solutions.skydev.pos.order_orchestrator_service.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * DTO for {@link }
 */
@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartCreateRequestDto {
    Object order;
}
