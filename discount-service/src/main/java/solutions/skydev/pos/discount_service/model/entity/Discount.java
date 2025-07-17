package solutions.skydev.pos.discount_service.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import solutions.skydev.pos.discount_service.model.enums.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Discount {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private DiscountType type;
    private String discountType; // e.g "employee", "coupon, "promotion" (Todo: currently unused)
    private BigDecimal value;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Double cap;
    private Double minSpend;
    private Integer minQty;
    private Integer maxQty;

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DiscountProduct> products = new ArrayList<>();

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DiscountLineItem> discountLineItems = new ArrayList<>();

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LineItemLevelDiscountOrder> lineItemLevelDiscountOrders = new ArrayList<>();

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderLevelDiscountOrder> orderLevelDiscountOrders = new ArrayList<>();
}
