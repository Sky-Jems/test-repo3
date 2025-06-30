package solutions.skydev.pos.common.discount_service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscountResponseDto {
    Long id;
    String name;
    String type;
    @JsonProperty("discount_type")
    String discountType;
    Double value;
    String scope;
    @JsonProperty("start_date_time")
    String startDateTime;
    @JsonProperty("end_date_time")
    String endDateTime;
    @JsonProperty("min_spend")
    Double minSpend;
    Double cap;
    @JsonProperty("min_qty")
    Integer minQty;
    @JsonProperty("max_qty")
    Integer maxQty;
}
