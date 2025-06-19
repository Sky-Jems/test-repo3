package solutions.skydev.pos.gateway_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class VariantOptionResponseDto implements Serializable {
    Long id;
    @JsonProperty("product_id")
    Long productId;
    String name;
}