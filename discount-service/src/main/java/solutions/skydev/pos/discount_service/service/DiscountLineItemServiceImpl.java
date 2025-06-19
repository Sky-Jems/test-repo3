package solutions.skydev.pos.discount_service.service;

import org.springframework.stereotype.Service;
import solutions.skydev.pos.discount_service.model.entity.DiscountLineItem;
import solutions.skydev.pos.discount_service.model.entity.DiscountOrder;
import solutions.skydev.pos.discount_service.repository.DiscountLineItemRepository;

import java.util.List;

@Service
public class DiscountLineItemServiceImpl implements DiscountLineItemService {

    DiscountLineItemRepository discountLineItemRepository;

    public DiscountLineItemServiceImpl(DiscountLineItemRepository discountLineItemRepository) {
        this.discountLineItemRepository = discountLineItemRepository;
    }

    @Override
    public DiscountLineItem save(DiscountLineItem discountLineItem) {
        if (discountLineItem == null) {
            throw new IllegalArgumentException("DiscountLineItem cannot be null");
        }

        DiscountLineItem existingDiscountLineItem = discountLineItemRepository.findByOrderId(discountLineItem.getOrderId());
        if (existingDiscountLineItem != null) {
            existingDiscountLineItem.setDiscount(discountLineItem.getDiscount());
            existingDiscountLineItem.setDiscountAmount(discountLineItem.getDiscountAmount());
            discountLineItem = discountLineItemRepository.save(existingDiscountLineItem);
            return discountLineItem;
        } else {
            return discountLineItemRepository.save(discountLineItem);
        }
    }

    public DiscountLineItem findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        List<DiscountLineItem> items = discountLineItemRepository.findAllByOrderId(id);

        return items.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("DiscountLineItem not found with orderId: " + id));
    }


}
