package solutions.skydev.pos.product_service.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import solutions.skydev.pos.product_service.model.entity.VariantOption;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link VariantOption}
 */
@Builder
@Jacksonized
@Data
public class VariantOptionRequestDtoList implements Serializable {

    @JsonProperty("variant_options")
    Set<VariantOptionRequestDto> variantOptionRequestDtoList;
}
