package solutions.skydev.pos.order_orchestrator_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import solutions.skydev.pos.order_orchestrator_service.model.enums.DiscountStatus;
import solutions.skydev.pos.order_orchestrator_service.model.enums.OrderStatus;
import solutions.skydev.pos.order_orchestrator_service.model.enums.PaymentStatus;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class OrderTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   
    Long id;
    Long orderId;
    
    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    DiscountStatus discountStatus = DiscountStatus.PENDING;
    
    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(precision = 10, scale = 2)
    BigDecimal grossAmount = BigDecimal.ZERO;
    
    @Column(precision = 10, scale = 2)
    BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    BigDecimal netAmount = BigDecimal.ZERO;
}
