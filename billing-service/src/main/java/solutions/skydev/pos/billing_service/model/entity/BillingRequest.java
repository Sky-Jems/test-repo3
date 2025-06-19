package solutions.skydev.pos.billing_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class BillingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal grossAmount = BigDecimal.ZERO;
    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;;
    @Column(precision = 10, scale = 2)
    private BigDecimal netAmount = BigDecimal.ZERO;
    
    private Long orderId;
}
