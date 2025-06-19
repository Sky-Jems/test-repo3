package solutions.skydev.pos.product_service.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import solutions.skydev.pos.product_service.model.entity.VariantOptionValue;

import java.io.Serializable;

/**
 * DTO for {@link VariantOptionValue}
 */
@Builder
@Jacksonized
@Data
public class VariantOptionValueRequestDto implements Serializable {
    Long id;
    String value;
    @JsonProperty("variant_option_id")
    Long variantOptionId;
}
