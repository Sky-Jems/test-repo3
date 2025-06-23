package solutions.skydev.pos.discount_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscountProductResponseDto {
    private Long id;
    @JsonProperty("product_id")
    private Long productId;
    @JsonProperty("discount_id")
    private Long discountId;
    private String type;
}
