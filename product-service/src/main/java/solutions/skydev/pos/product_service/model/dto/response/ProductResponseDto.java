package solutions.skydev.pos.product_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import solutions.skydev.pos.product_service.model.entity.Product;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Product}
 */
@Builder
@Value
public class ProductResponseDto implements Serializable {
    Long id;
    String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String description;
    List<CategoryResponseDto> categories;
}
