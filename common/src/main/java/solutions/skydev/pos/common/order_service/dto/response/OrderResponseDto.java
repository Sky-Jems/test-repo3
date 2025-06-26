package solutions.skydev.pos.common.order_service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto implements Serializable {
    Long id;
    String customer;
    BigDecimal total;
    
    @JsonProperty("table_number")
    String tableNumber;
    
    @JsonProperty("line_items")
    List<LineItemResponseDto> lineItems;
}
