package solutions.skydev.pos.product_service.model.dto.response;

import lombok.Value;

import java.io.Serializable;

@Value
public class ProductVariantResponseDto implements Serializable {
    Long variantId;
    String sku;
    Double price;
}
