package solutions.skydev.pos.order_orchestrator_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Builder
@Jacksonized
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResponseDto {
    Long id;
    String customer;
    @JsonProperty("table_number")
    String tableNumber;
    BigDecimal total;
}
