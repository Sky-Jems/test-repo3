package solutions.skydev.pos.discount_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import solutions.skydev.pos.discount_service.model.enums.DiscountScope;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@DiscriminatorValue("LINE_ITEM")
public class LineItemLevelDiscountOrder extends DiscountOrder {

    @Column(nullable = false)
    private Long lineItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", nullable = false)
    private Discount discount;

    @Transient
    private BigDecimal price;

    @Transient
    private Integer quantity;

    @Transient
    private BigDecimal subTotal;

    @Override
    public DiscountScope getScope() {
        return DiscountScope.LINE_ITEM;
    }
}
