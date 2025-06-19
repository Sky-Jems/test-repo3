package solutions.skydev.pos.product_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import solutions.skydev.pos.product_service.model.entity.VariantOption;

import java.io.Serializable;

/**
 * DTO for {@link VariantOption}
 */
@Value
public class VariantOptionResponseDto implements Serializable {
    Long id;
    @JsonProperty("product_id")
    Long productId;
    String name;
}