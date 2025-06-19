package solutions.skydev.pos.gateway_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Builder;

import java.io.Serializable;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VariantOptionValueResponseDto implements Serializable {
    Long id;
    String value;
    Long variantOptionId;

    @JsonProperty("variant_option")
    VariantOptionResponseDto variantOption;
}
