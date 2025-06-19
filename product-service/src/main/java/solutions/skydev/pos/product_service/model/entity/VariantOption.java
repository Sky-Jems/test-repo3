package solutions.skydev.pos.product_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class VariantOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    Product product;
    
    private String name;

    @OneToMany(mappedBy = "variantOption", cascade = CascadeType.ALL)
    private Set<VariantOptionValue> values = new HashSet<>();
}
