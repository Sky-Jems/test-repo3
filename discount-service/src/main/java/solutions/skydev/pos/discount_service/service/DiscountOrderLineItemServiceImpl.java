//package solutions.skydev.pos.discount_service.service;
//
//import org.springframework.stereotype.Service;
//import solutions.skydev.pos.discount_service.repository.DiscountOrderLineItemRepository;
//
//@Service
//public class DiscountOrderLineItemServiceImpl implements DiscountOrderLineItemService {
//
//    private final DiscountOrderLineItemRepository discountOrderLineItemRepository;
//
//    public DiscountOrderLineItemServiceImpl(DiscountOrderLineItemRepository discountOrderLineItemRepository) {
//        this.discountOrderLineItemRepository = discountOrderLineItemRepository;
//    }
//
//    @Override
//    public DiscountOrderLineItem save(DiscountOrderLineItem discountOrderLineItem) {
//        if (discountOrderLineItem == null) {
//            throw new IllegalArgumentException("DiscountOrderLineItem cannot be null");
//        }
//        return discountOrderLineItemRepository.save(discountOrderLineItem);
//    }
//}
