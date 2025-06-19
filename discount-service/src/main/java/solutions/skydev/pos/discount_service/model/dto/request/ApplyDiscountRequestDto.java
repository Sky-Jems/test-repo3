package solutions.skydev.pos.discount_service.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.util.List;

@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyDiscountRequestDto implements Serializable {
    @JsonProperty("discount_id")
    Long discountId;
    @JsonProperty("order_id")
    Long orderId;
    @JsonProperty("total_amount")
    Double totalAmount;
    @JsonProperty("line_items")
    List<LineItemDto> lineItems;
}
