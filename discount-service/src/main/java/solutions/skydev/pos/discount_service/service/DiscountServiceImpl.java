package solutions.skydev.pos.discount_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.discount_service.model.entity.*;
import solutions.skydev.pos.discount_service.repository.DiscountRepository;
import solutions.skydev.pos.discount_service.service.strategy.DiscountStrategy;
import solutions.skydev.pos.discount_service.service.strategy.DiscountStrategyResolver;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private DiscountLineItemServiceImpl discountLineItemService;

    @Autowired
    private DiscountOrderServiceImpl discountOrderService;

    private final DiscountStrategyResolver strategyResolver;
    private final DiscountRepository discountRepository;

    public DiscountServiceImpl(DiscountStrategyResolver strategyResolver, DiscountRepository discountRepository) {
        this.strategyResolver = strategyResolver;
        this.discountRepository = discountRepository;
    }

    @Override
    public DiscountLineItem applyDiscount(Order order, Discount discount) {
        DiscountOrder existingDiscountOrder = discountOrderService.findByOrderId(order.getId());

        if (existingDiscountOrder == null) {
            return handleNoDiscount(order);
        }

        Discount fullDiscount = findById(existingDiscountOrder.getDiscount().getId());

        if (!isWithinValidTime(fullDiscount)) {
            return handleNoDiscount(order);
        }

        List<DiscountProduct> products = fullDiscount.getProducts();
        DiscountStrategy strategy = strategyResolver.resolve(fullDiscount.getType());

        double discountAmount = strategy.calculateDiscount(order, fullDiscount, products);
        discountAmount = applyCap(discountAmount, fullDiscount.getCap());

        DiscountLineItem discountLineItem = createLineItem(order, fullDiscount, discountAmount);
        return discountLineItemService.save(discountLineItem);
    }

    private double applyCap(double discountAmount, double cap) {
        return Math.min(discountAmount, cap);
    }

    private boolean isWithinValidTime(Discount discount) {
        LocalDateTime now = LocalDateTime.now();
        return (discount.getStartDateTime() == null || !now.isBefore(discount.getStartDateTime())) &&
                (discount.getEndDateTime() == null || !now.isAfter(discount.getEndDateTime()));
    }

    private DiscountLineItem createLineItem(Order order, Discount discount, double discountAmount) {
        DiscountLineItem lineItem = new DiscountLineItem();
        lineItem.setOrderId(order.getId());
        lineItem.setDiscountAmount(discountAmount);
        lineItem.setDiscount(discount);
        return lineItem;
    }

    private DiscountLineItem handleNoDiscount(Order order) {
        DiscountLineItem lineItem = new DiscountLineItem();
        lineItem.setOrderId(order.getId());
        lineItem.setDiscountAmount(0.0);
        return lineItem;
    }

    @Override
    public Discount createDiscount(Discount discount) {
        if (discount.getProducts() != null) {
            for (DiscountProduct product : discount.getProducts()) {
                product.setDiscount(discount);
            }
        }
        discountRepository.save(discount);
        return discount;
    }

    @Override
    public Discount findById(Long id) {
        return discountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found with id: " + id));
    }

    @Override
    public List<Discount> findAll() {
        return discountRepository.findAll();
    }

    @Override
    public Discount updateDiscount(Discount discount) {
        Discount existingDiscount = findById(discount.getId());
        existingDiscount.setName(discount.getName());
        existingDiscount.setType(discount.getType());
        existingDiscount.setCap(discount.getCap());
        existingDiscount.setStartDateTime(discount.getStartDateTime());
        existingDiscount.setEndDateTime(discount.getEndDateTime());
        existingDiscount.setValue(discount.getValue());
        existingDiscount.setScope(discount.getScope());
        existingDiscount.setDiscountType(discount.getDiscountType());

        discountRepository.save(existingDiscount);

        return existingDiscount;
    }

    @Override
    public Discount deleteDiscount(Long id) {
        Discount discount = findById(id);
        discountRepository.delete(discount);
        return discount;
    }

    @Override
    public List<DiscountProduct> getProductsByDiscountId(Long id) {
        Discount discount = findById(id);
        return discount.getProducts();
    }
}
