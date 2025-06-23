package solutions.skydev.pos.discount_service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineItem {

    private Long id;
    private Long productId;
    private Integer quantity;
    private Double price;
    private Double subTotal;
}
