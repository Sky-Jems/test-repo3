package solutions.skydev.pos.product_service.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Variant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Product product;

    String sku;
    Double price;
}
