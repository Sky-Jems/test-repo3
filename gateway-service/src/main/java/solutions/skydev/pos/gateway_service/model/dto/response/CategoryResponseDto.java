package solutions.skydev.pos.gateway_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Builder
@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponseDto implements Serializable {
    Long id;
    String name;
}
