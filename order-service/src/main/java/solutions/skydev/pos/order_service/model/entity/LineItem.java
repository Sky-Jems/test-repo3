package solutions.skydev.pos.order_service.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class LineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Integer quantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(precision = 10, scale = 2)
    private BigDecimal subTotal = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private void calculateSubTotal() {
        BigDecimal subTotal = BigDecimal.ZERO;
        if (price != null && quantity != null) {
            subTotal = price.multiply(BigDecimal.valueOf(quantity));
        }
        this.subTotal = subTotal;
    }

    @PrePersist
    @PreUpdate
    private void prePersistAndUpdate() {
        this.calculateSubTotal();
        if (this.order != null) {
            this.order.calculateTotal();
        }
    }
}
