package solutions.skydev.pos.discount_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import solutions.skydev.pos.discount_service.model.enums.DiscountScope;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@DiscriminatorValue("ORDER")
public class OrderLevelDiscountOrder extends DiscountOrder {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", nullable = false)
    private Discount discount;

    @Override
    public DiscountScope getScope() {
        return DiscountScope.ORDER;
    }
}
