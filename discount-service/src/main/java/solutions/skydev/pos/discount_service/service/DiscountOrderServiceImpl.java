package solutions.skydev.pos.discount_service.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.common.discount_service.dto.response.LineItemLevelDiscountOrderResponseDto;
import solutions.skydev.pos.discount_service.model.DiscountOrderSummary;
import solutions.skydev.pos.discount_service.model.entity.*;
import solutions.skydev.pos.discount_service.repository.DiscountOrderRepository;
import solutions.skydev.pos.discount_service.repository.DiscountRepository;
import solutions.skydev.pos.discount_service.repository.LineItemLevelDiscountOrderRepository;
import solutions.skydev.pos.discount_service.service.strategy.DiscountStrategyResolver;
import solutions.skydev.pos.discount_service.service.strategy.scope.DiscountScopeStrategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DiscountOrderServiceImpl implements DiscountOrderService {

    private final DiscountOrderRepository discountOrderRepository;
    private final DiscountStrategyResolver strategyResolver;
    private final DiscountRepository discountRepository;
    private final LineItemLevelDiscountOrderRepository lineItemLevelDiscountOrderRepository;

    public DiscountOrderServiceImpl(DiscountOrderRepository discountOrderRepository,
                                    DiscountStrategyResolver strategyResolver,
                                    DiscountRepository discountRepository,
                                    LineItemLevelDiscountOrderRepository lineItemLevelDiscountOrderRepository) {
        this.discountOrderRepository = discountOrderRepository;
        this.strategyResolver = strategyResolver;
        this.discountRepository = discountRepository;
        this.lineItemLevelDiscountOrderRepository = lineItemLevelDiscountOrderRepository;
    }

    @Override
    public DiscountOrderSummary getDiscountOrderSummary(Long orderId) {
        List<DiscountOrder> allDiscounts = discountOrderRepository.findAllByOrderId(orderId);

        BigDecimal discountAmount = calculateTotalDiscountAmount(allDiscounts);
        Long discountId = extractOrderLevelDiscountId(allDiscounts);

        return DiscountOrderSummary.builder()
                .orderId(orderId)
                .discountAmount(discountAmount)
                .discountId(discountId)
                .discountOrders(allDiscounts)
                .build();
    }

    private BigDecimal calculateTotalDiscountAmount(List<DiscountOrder> discountOrders) {
        return discountOrders.stream()
                .filter(d -> d instanceof OrderLevelDiscountOrder)
                .map(DiscountOrder::getDiscountAmount)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(() ->
                        discountOrders.stream()
                                .filter(d -> d instanceof LineItemLevelDiscountOrder)
                                .map(DiscountOrder::getDiscountAmount)
                                .filter(Objects::nonNull)
                                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private Long extractOrderLevelDiscountId(List<DiscountOrder> discountOrders) {
        return discountOrders.stream()
                .filter(d -> d instanceof OrderLevelDiscountOrder)
                .map(d -> d.getDiscount().getId())
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<LineItemLevelDiscountOrder> deleteByLineItems(Long orderId, List<LineItemLevelDiscountOrder> lineItemIds) {
        if (orderId == null || lineItemIds == null || lineItemIds.isEmpty()) {
            throw new IllegalArgumentException("Order ID and line item IDs cannot be null or empty.");
        }

        List<LineItemLevelDiscountOrder> deletedDiscounts = new ArrayList<>();
        for (LineItemLevelDiscountOrder lineItem : lineItemIds) {
            List<LineItemLevelDiscountOrder> discounts =
                    lineItemLevelDiscountOrderRepository.findAllByOrderIdAndLineItemId(orderId, lineItem.getLineItemId());
            deletedDiscounts.addAll(discounts);
            lineItemLevelDiscountOrderRepository.deleteAll(discounts);
        }
        return deletedDiscounts;
    }

    @Override
    public DiscountOrder create(OrderLevelDiscountOrder discountOrder, Order order) {
        if (discountOrder == null || discountOrder.getDiscount() == null) {
            throw new IllegalArgumentException("Order-level discount or discount reference cannot be null.");
        }

        Discount discount = discountRepository.findById(discountOrder.getDiscount().getId())
                .orElseThrow(() -> new IllegalArgumentException("Discount not found with ID: " + discountOrder.getDiscount().getId()));

        DiscountOrder existing = discountOrderRepository.findFirstByOrderId(order.getId());
        OrderLevelDiscountOrder toSave;

        if (existing != null) {
            if (!(existing instanceof OrderLevelDiscountOrder)) {
                throw new IllegalArgumentException("Existing discount for orderId " + order.getId() + " is not of scope ORDER.");
            }
            toSave = (OrderLevelDiscountOrder) existing;
        } else {
            toSave = discountOrder;
        }

        toSave.setOrderId(order.getId());
        toSave.setDiscount(discount);

        DiscountScopeStrategy strategy = strategyResolver.resolve(
                discountOrder.getScope(),
                discount.getType(),
                discount.getValue()
        );

        BigDecimal discountAmount = strategy.applyDiscount(order, discountOrder);

        toSave.setDiscountAmount(discountAmount);

        return discountOrderRepository.save(toSave);
    }

    @Override
    @Transactional
    public List<LineItemLevelDiscountOrder> create(List<LineItemLevelDiscountOrder> discountOrders, Long orderId) {
        if (discountOrders == null || discountOrders.isEmpty()) {
            throw new IllegalArgumentException("No line-item discounts provided.");
        }

        DiscountOrder existing = discountOrderRepository.findFirstByOrderId(orderId);
        if (existing != null && !(existing instanceof LineItemLevelDiscountOrder)) {
            throw new IllegalArgumentException(
                    "Cannot create LINE_ITEM-level discount: ORDER-level discount already exists for orderId " + orderId);
        }

        List<LineItemLevelDiscountOrder> results = new ArrayList<>();
        for (LineItemLevelDiscountOrder item : discountOrders) {

            LineItemLevelDiscountOrder toSave = lineItemLevelDiscountOrderRepository
                    .findByOrderIdAndLineItemId(orderId, item.getLineItemId())
                    .orElseGet(LineItemLevelDiscountOrder::new);

            Discount discount;
            if (item.getDiscount() == null) {
                if (toSave.getId() == null) {
                    continue;
                }
                discount = toSave.getDiscount();
                if (discount == null) {
                    throw new IllegalStateException(
                            "Existing line-item found but has no discount stored.");
                }
            } else {
                discount = discountRepository.findById(item.getDiscount().getId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Discount not found with ID: " + item.getDiscount().getId()));
            }

            toSave.setOrderId(orderId);
            toSave.setLineItemId(item.getLineItemId());
            toSave.setDiscount(discount);
            toSave.setPrice(item.getPrice());
            toSave.setQuantity(item.getQuantity());
            toSave.setSubTotal(item.getSubTotal());

            DiscountScopeStrategy strategy = strategyResolver.resolve(
                    item.getScope(),
                    discount.getType(),
                    discount.getValue()
            );

            BigDecimal discountAmount = strategy.applyDiscount(null, toSave);
            toSave.setDiscountAmount(discountAmount);

            results.add(toSave);
        }

        return discountOrderRepository.saveAll(results);
    }

    @Override
    public DiscountOrder findByOrderId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        return discountOrderRepository.findByOrderId(id);
    }

    public List<DiscountOrder> deleteByOrderId(Long orderId) {
        List<DiscountOrder> discounts = discountOrderRepository.findAllByOrderId(orderId);
        discountOrderRepository.deleteAll(discounts);
        return discounts;
    }
}
