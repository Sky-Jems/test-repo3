package solutions.skydev.pos.product_service.model.dto.response;

import lombok.Value;

import java.io.Serializable;

@Value
public class CategoryResponseDto implements Serializable {
    Long id;
    String name;
}
