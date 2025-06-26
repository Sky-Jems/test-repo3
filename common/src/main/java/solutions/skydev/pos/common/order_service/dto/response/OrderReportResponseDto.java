package solutions.skydev.pos.common.order_service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;


@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class OrderReportResponseDto implements Serializable {
    List<OrderResponseDto> orders;
    @JsonProperty("total_orders")
    Long totalOrders;
    @JsonProperty("amount_of_sales")
    Double amountOfSales;
}
