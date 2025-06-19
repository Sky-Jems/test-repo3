package solutions.skydev.pos.gateway_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value
public class ProductResponseDto implements Serializable {
    Long id;
    String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String description;
    List<CategoryResponseDto> categories;
}

