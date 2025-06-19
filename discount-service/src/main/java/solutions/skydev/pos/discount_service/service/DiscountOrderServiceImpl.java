package solutions.skydev.pos.discount_service.service;

import org.springframework.stereotype.Service;
import solutions.skydev.pos.discount_service.model.entity.DiscountOrder;
import solutions.skydev.pos.discount_service.producer.DiscountOrderProducer;
import solutions.skydev.pos.discount_service.repository.DiscountOrderRepository;

@Service
public class DiscountOrderServiceImpl implements DiscountOrderService {

    private final DiscountOrderRepository discountOrderRepository;

    public DiscountOrderServiceImpl(DiscountOrderRepository discountOrderRepository) {
        this.discountOrderRepository = discountOrderRepository;
    }

    @Override
    public DiscountOrder create(DiscountOrder discountOrder) {
        if (discountOrder == null) {
            throw new IllegalArgumentException("Discount order cannot be null");
        }

        if (discountOrder.getOrderId() == null || discountOrder.getDiscount() == null) {
            throw new IllegalArgumentException("Discount order must have an associated order and discount");
        }

        // only one discount can be applied to an order at a time for now
        DiscountOrder existingDiscountOrder = discountOrderRepository.findByOrderId(discountOrder.getOrderId());
        if (existingDiscountOrder != null) {
            existingDiscountOrder.setDiscount(discountOrder.getDiscount());
            discountOrder = discountOrderRepository.save(existingDiscountOrder);
            return discountOrder;
        } else {
            return discountOrderRepository.save(discountOrder);
        }


    }

    @Override
    public DiscountOrder findByOrderId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        return discountOrderRepository.findByOrderId(id);
    }

    @Override
    public DiscountOrder deleteByOrderId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }

        DiscountOrder discountOrder = discountOrderRepository.findByOrderId(id);
        if (discountOrder == null) {
            throw new IllegalArgumentException("No discount order found for the given order ID");
        }

        discountOrderRepository.delete(discountOrder);
        return discountOrder;

    }
}
