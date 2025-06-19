package solutions.skydev.pos.product_service.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class VariantOptionValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private VariantOption variantOption;

    private String value;
}
