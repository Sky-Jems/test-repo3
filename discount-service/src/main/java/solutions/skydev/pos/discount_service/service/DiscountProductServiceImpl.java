package solutions.skydev.pos.discount_service.service;

import org.springframework.stereotype.Service;
import solutions.skydev.pos.discount_service.model.entity.DiscountProduct;
import solutions.skydev.pos.discount_service.repository.DiscountProductRepository;

import java.util.List;

@Service
public class DiscountProductServiceImpl implements DiscountProductService {

    private final DiscountProductRepository discountProductRepository;

    public DiscountProductServiceImpl(DiscountProductRepository discountProductRepository) {
        this.discountProductRepository = discountProductRepository;
    }

    @Override
    public DiscountProduct save(DiscountProduct discountProduct) {
        discountProductRepository.save(discountProduct);
        return discountProduct;
    }

    @Override
    public DiscountProduct deleteById(DiscountProduct discountProduct) {
        if (discountProduct == null || discountProduct.getId() == null) {
            throw new IllegalArgumentException("Discount product or its ID cannot be null");
        }
        discountProductRepository.deleteById(discountProduct.getId());
        return discountProduct;
    }

    @Override
    public List<DiscountProduct> findAllByDiscountId(Long discountId) {
        return null;
    }


    public List<DiscountProduct> getAllByDiscountId(Long discountId) {
        if (discountId == null) {
            throw new IllegalArgumentException("Discount ID cannot be null");
        }
        return discountProductRepository.findAllByDiscount_Id(discountId);
    }
}
