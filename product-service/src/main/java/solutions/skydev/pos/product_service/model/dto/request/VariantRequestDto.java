package solutions.skydev.pos.product_service.model.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import solutions.skydev.pos.product_service.model.entity.Variant;

import java.io.Serializable;


/**
 * DTO for {@link Variant}
 * */
@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VariantRequestDto implements Serializable {
    Long id;
    @JsonProperty("product_id")
    Long productId;
    String sku;
    Double price;
}
