package solutions.skydev.pos.gateway_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VariantResponseDto implements Serializable {
    @JsonProperty("variant_id")
    Long variantId;
    String sku;
    Double price;

    ProductResponseDto product;

    @JsonProperty("variant_combinations")
    List<VariantOptionValueResponseDto> variantCombinations;
}
