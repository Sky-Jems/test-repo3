package solutions.skydev.pos.common.product_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponseDto implements Serializable {
    Long id;
    String name;
    String description;
    BigDecimal price;
    @JsonProperty("category_ids")
    List<Long> categoryIds;
    
    List<CategoryResponseDto> categories;
}
