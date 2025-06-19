package solutions.skydev.pos.product_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import solutions.skydev.pos.product_service.model.entity.Variant;

import java.io.Serializable;

/**
 * DTO link for {@link Variant}
 * */
@Value
public class VariantResponseDto implements Serializable {
    @JsonProperty("variant_id")
    Long variantId;
    ProductResponseDto product;
    String sku;
    Double price;
}