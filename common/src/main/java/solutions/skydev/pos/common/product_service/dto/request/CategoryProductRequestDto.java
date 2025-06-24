package solutions.skydev.pos.common.product_service.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryProductRequestDto implements Serializable {
    @JsonProperty("category_id")
    String categoryId;
    @JsonProperty("product_id")
    String productId;
}
