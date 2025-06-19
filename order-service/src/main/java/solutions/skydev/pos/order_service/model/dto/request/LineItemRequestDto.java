package solutions.skydev.pos.order_service.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;
import solutions.skydev.pos.order_service.model.entity.LineItem;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link LineItem}
 */
@Builder
@Jacksonized
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
