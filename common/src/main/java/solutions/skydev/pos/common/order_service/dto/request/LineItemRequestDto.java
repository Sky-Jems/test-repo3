package solutions.skydev.pos.common.order_service.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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
