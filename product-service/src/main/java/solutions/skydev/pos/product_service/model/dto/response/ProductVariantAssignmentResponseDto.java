package solutions.skydev.pos.product_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import solutions.skydev.pos.product_service.model.entity.ProductVariantAssignment;

import java.io.Serializable;

/**
 * DTO for {@link ProductVariantAssignment}
 */
@Value
public class ProductVariantAssignmentResponseDto implements Serializable {
    @JsonProperty("assignment_id")
    Long id;
    
    @JsonProperty("variant_id")
    Long variantId;
    
    @JsonProperty("variant_option_value_id")
    Long variantOptionValueId;
}