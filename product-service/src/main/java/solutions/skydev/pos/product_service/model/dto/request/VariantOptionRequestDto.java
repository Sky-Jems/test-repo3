package solutions.skydev.pos.product_service.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import solutions.skydev.pos.product_service.model.entity.VariantOption;
import solutions.skydev.pos.product_service.model.entity.VariantOptionValue;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link VariantOption}
 */
@Builder
@Jacksonized
@Data
public class VariantOptionRequestDto implements Serializable {
    Long id;
    String name;
    @JsonProperty("product_id")
    Long productId;

    Set<VariantOptionValue> values;
}
