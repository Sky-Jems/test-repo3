package solutions.skydev.pos.common.product_service.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    Long id;
    String name;
    String description;
    Double price;
    
    @JsonProperty("category_ids")
    List<Long> categoryIds;
}