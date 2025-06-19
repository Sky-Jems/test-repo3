package solutions.skydev.pos.order_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "order_")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customer;
    private String tableNumber;
    @Column(precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<LineItem> lineItems = new ArrayList<>();

    // This is as convenient if update calls are wrapped in a transaction
    // TODO: need to consider moving this to the service layer
    public void calculateTotal() {
        total = lineItems.stream()
                .map(LineItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
