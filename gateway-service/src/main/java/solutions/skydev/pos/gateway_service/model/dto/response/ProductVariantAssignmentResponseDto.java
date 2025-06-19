package solutions.skydev.pos.gateway_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductVariantAssignmentResponseDto implements Serializable {
    @JsonProperty("assignment_id")
    Long id;
    
    @JsonProperty("variant_id")
    Long variantId;
    
    @JsonProperty("variant_option_value_id")
    Long variantOptionValueId;
    
    @JsonProperty("variant_option")
    VariantOptionResponseDto variantOption;
}