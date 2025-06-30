package solutions.skydev.pos.common.discount_service.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscountProductRequestDto {
    Long id;
    @JsonProperty("discount_id")
    Long discountId;
    @JsonProperty("product_id")
    Long productId;
}
