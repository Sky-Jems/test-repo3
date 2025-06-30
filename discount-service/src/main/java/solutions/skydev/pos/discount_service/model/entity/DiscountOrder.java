package solutions.skydev.pos.discount_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import solutions.skydev.pos.discount_service.model.enums.DiscountScope;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
public abstract class DiscountOrder {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private Long orderId;

    @Column(nullable = false, columnDefinition = "DECIMAL(19,2) DEFAULT 0.00")
    private BigDecimal discountAmount = BigDecimal.ZERO;;

    public abstract DiscountScope getScope();
    public abstract Discount getDiscount();
}
