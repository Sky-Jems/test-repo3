package solutions.skydev.pos.discount_service.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import solutions.skydev.pos.discount_service.model.enums.DiscountScope;
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
    // IMPLEMENTATION NOTE: Only one discount can be applied for now and it is applied to the whole order.
    // Todo: In the future, we can extend this to allow multiple discounts to be applied to an order or specific line items.
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String name; // e.g., "Employee Discount 50%", "Father's Day Special Applied to Selected Items"
    @Enumerated(EnumType.STRING)
    private DiscountType type; // e.g., "percentage", "fixed_value"
    private String discountType; // e.g "employee", "coupon, "promotion" (Todo: currently unused)
    private BigDecimal value; // e.g., 10.0 for 10% or 10.0 for 10 off
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Double cap; // for 5k capping, this is the maximum discount amount that can be applied to the order
    private Double minSpend;
    private Integer minQty;
    private Integer maxQty;

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DiscountProduct> products = new ArrayList<>();

//    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private List<DiscountOrder> discountOrders = new ArrayList<>();

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DiscountLineItem> discountLineItems = new ArrayList<>();

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LineItemLevelDiscountOrder> lineItemLevelDiscountOrders = new ArrayList<>();

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderLevelDiscountOrder> orderLevelDiscountOrders = new ArrayList<>();

//    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private List<DiscountOrderLineItem> discountOrderLineItems = new ArrayList<>();
}
