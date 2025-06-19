package solutions.skydev.pos.product_service.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import solutions.skydev.pos.product_service.model.entity.Product;

import java.io.Serializable;
import java.util.Set;
import java.util.Optional;

/**
 * DTO for {@link Product}
 */
@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequestDto implements Serializable {
    Long id;
    String name;
    String description;

    Optional<Set<Long>> categories;
}
