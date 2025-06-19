package solutions.skydev.pos.discount_service.model.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import solutions.skydev.pos.discount_service.model.enums.DiscountType;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class DiscountVariant {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private Long variantId;

    @Nullable
    private DiscountType type; // e.g., "percentage", "fixed_amount", "null"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", nullable = true) // nullable to allow for independent variant options without a discount campaign tied to it
    private Discount discount;
}
