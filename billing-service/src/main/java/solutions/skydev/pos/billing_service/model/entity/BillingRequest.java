package solutions.skydev.pos.billing_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class BillingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal netAmount = BigDecimal.ZERO;
    
    @CreationTimestamp
    OffsetDateTime createdAt = OffsetDateTime.now();
    
    private Long orderId;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal remainingAmount = BigDecimal.ZERO;
    
    @PreUpdate
    @PrePersist
    private void calculateRemainingAmount() {
        if (netAmount == null || paidAmount == null) {
            remainingAmount = BigDecimal.ZERO;
            return;
        }
        remainingAmount = netAmount.subtract(paidAmount);
    }
}
