package solutions.skydev.pos.product_service.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"variant_id", "variant_option_value_id"})
})
public class ProductVariantAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Variant variant;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    VariantOptionValue variantOptionValue;
}
