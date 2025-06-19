package solutions.skydev.pos.product_service.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

import lombok.extern.jackson.Jacksonized;
import solutions.skydev.pos.product_service.model.entity.Category;

/**
 * DTO for {@link Category}
 */
@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryRequestDto implements Serializable {
    Long id;
    String name;
}