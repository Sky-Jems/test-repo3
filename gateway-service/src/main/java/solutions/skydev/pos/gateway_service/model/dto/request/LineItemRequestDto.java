package solutions.skydev.pos.gateway_service.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * DTO for line item requests
 */
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class LineItemRequestDto implements Serializable {
    Long id;
    @JsonProperty("product_id")
    Long productId;
    Integer quantity;
    Double price;
    @JsonProperty("order_id")
    Long orderId;

}
