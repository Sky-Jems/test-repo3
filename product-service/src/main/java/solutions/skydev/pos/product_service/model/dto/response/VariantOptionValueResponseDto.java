package solutions.skydev.pos.product_service.model.dto.response;

import lombok.Value;
import solutions.skydev.pos.product_service.model.entity.VariantOptionValue;

import java.io.Serializable;

/**
 * DTO for {@link VariantOptionValue}
 */
@Value
public class VariantOptionValueResponseDto implements Serializable {
    Long id;
    String value;
    Long variantOptionId;
}
