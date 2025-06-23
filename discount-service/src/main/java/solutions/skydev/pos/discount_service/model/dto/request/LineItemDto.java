package solutions.skydev.pos.discount_service.model.dto.request;

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
public class LineItemDto implements Serializable {
    @JsonProperty("product_id")
    String productId;
    @JsonProperty("category_id")
    String categoryId;
    int quantity;
    Double price;
}
