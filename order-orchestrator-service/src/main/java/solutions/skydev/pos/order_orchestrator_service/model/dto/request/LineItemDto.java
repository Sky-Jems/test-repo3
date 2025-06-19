package solutions.skydev.pos.order_orchestrator_service.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineItemDto implements Serializable {
    String variantId;
    String productId;
    String categoryId;
    int quantity;
    Double price;
}
