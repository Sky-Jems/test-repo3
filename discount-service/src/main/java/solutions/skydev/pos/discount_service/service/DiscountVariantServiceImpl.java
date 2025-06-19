package solutions.skydev.pos.discount_service.service;

import org.springframework.stereotype.Service;
import solutions.skydev.pos.discount_service.model.entity.DiscountVariant;
import solutions.skydev.pos.discount_service.repository.DiscountVariantRepository;

import java.util.List;

@Service
public class DiscountVariantServiceImpl implements DiscountVariantService {

    private final DiscountVariantRepository discountVariantRepository;

    public DiscountVariantServiceImpl(DiscountVariantRepository discountVariantRepository) {
        this.discountVariantRepository = discountVariantRepository;
    }

    @Override
    public DiscountVariant save(DiscountVariant discountVariant) {
        discountVariantRepository.save(discountVariant);
        return discountVariant;
    }

    @Override
    public DiscountVariant deleteById(DiscountVariant discountVariant) {
        if (discountVariant == null || discountVariant.getId() == null) {
            throw new IllegalArgumentException("Discount variant or its ID cannot be null");
        }
        discountVariantRepository.deleteById(discountVariant.getId());
        return discountVariant;
    }

    @Override
    public List<DiscountVariant> findAllByDiscountId(Long discountId) {
        return null;
    }


    public List<DiscountVariant> getAllByDiscountId(Long discountId) {
        if (discountId == null) {
            throw new IllegalArgumentException("Discount ID cannot be null");
        }
        return discountVariantRepository.findAllByDiscount_Id(discountId);
    }
}
