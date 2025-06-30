package solutions.skydev.pos.discount_service.service;

import org.springframework.stereotype.Service;
import solutions.skydev.pos.discount_service.model.entity.*;
import solutions.skydev.pos.discount_service.repository.DiscountRepository;
import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;

    public DiscountServiceImpl(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
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
