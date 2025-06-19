package solutions.skydev.pos.gateway_service.model.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class OrderResponseDto implements Serializable {
    Long id;
    String customer;
    @JsonProperty("table_number")
    String tableNumber;
    BigDecimal total;
    @JsonProperty("line_items")
    List<LineItemResponseDto> lineItems;
}
