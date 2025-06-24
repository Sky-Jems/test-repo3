package solutions.skydev.pos.common.product_service.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;


@Builder
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryRequestDto implements Serializable {
    Long id;
    String name;
}