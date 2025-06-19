package solutions.skydev.pos.discount_service.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscountRequestDto {
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
    Double cap;

    List<DiscountVariantRequestDto> variants;
}
