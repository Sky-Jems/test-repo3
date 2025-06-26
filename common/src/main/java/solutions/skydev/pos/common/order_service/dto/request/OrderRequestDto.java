package solutions.skydev.pos.common.order_service.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.util.List;

@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class OrderRequestDto implements Serializable {
    Long id;
    String customer;
    @JsonProperty("table_number")
    String tableNumber;
    @JsonProperty("line_items")
    List<LineItemRequestDto> lineItems;
}
