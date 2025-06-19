package solutions.skydev.pos.order_orchestrator_service.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for line item requests
 */
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LineItemRequestDto implements Serializable {
    Long id;
    @JsonProperty("product_id")
    Long productId;
    Integer quantity;
    Double price;
    @JsonProperty("order_id")
    Long orderId;

}
