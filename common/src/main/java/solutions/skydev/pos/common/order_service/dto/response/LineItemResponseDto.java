package solutions.skydev.pos.common.order_service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import solutions.skydev.pos.common.product_service.dto.response.ProductResponseDto;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class LineItemResponseDto implements Serializable {
    Long id;
    @JsonProperty("product_id")
    Long productId;
    Integer quantity;
    Double price;
    @JsonProperty("sub_total")
    BigDecimal subTotal;
    @JsonProperty("order_id")
    Long orderId;
    
    // TODO: Maybe make another DTO with enriched product details
    ProductResponseDto product;
}
