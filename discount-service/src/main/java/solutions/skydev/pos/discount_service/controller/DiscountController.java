package solutions.skydev.pos.discount_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solutions.skydev.pos.discount_service.model.dto.response.DiscountLineItemResponseDto;
import solutions.skydev.pos.discount_service.model.dto.response.DiscountResponseDto;
import solutions.skydev.pos.discount_service.model.dto.response.DiscountVariantResponseDto;
import solutions.skydev.pos.discount_service.model.entity.Discount;
import solutions.skydev.pos.discount_service.model.entity.DiscountLineItem;
import solutions.skydev.pos.discount_service.model.entity.DiscountVariant;
import solutions.skydev.pos.discount_service.model.mapper.DiscountLineItemMapper;
import solutions.skydev.pos.discount_service.model.mapper.DiscountMapper;
import solutions.skydev.pos.discount_service.model.mapper.DiscountVariantMapper;
import solutions.skydev.pos.discount_service.service.DiscountLineItemService;
import solutions.skydev.pos.discount_service.service.DiscountService;

import java.util.List;

@RestController
@RequestMapping("/discounts")
public class DiscountController {

    private final DiscountService discountService;
    private final DiscountLineItemService discountLineItemService;
    private final DiscountMapper discountMapper;
    private final DiscountLineItemMapper discountLineItemMapper;
    private final DiscountVariantMapper discountVariantMapper;

    public DiscountController(DiscountService discountService,
                              DiscountMapper discountMapper,
                              DiscountLineItemService discountLineItemService,
                              DiscountLineItemMapper discountLineItemMapper,
                              DiscountVariantMapper discountVariantMapper) {
        this.discountVariantMapper = discountVariantMapper;
        this.discountLineItemService = discountLineItemService;
        this.discountService = discountService;
        this.discountMapper = discountMapper;
        this.discountLineItemMapper = discountLineItemMapper;
    }

    @GetMapping
    public List<DiscountResponseDto> getAllDiscounts() {
        List<Discount> discounts = discountService.findAll();
        return discountMapper.toDto(discounts);
    }

    @GetMapping("/{id}")
    public DiscountResponseDto getDiscountById(@PathVariable Long id) {
        Discount discount = discountService.findById(id);
        return discountMapper.toDto(discount);
    }

    @GetMapping("/{id}/variants")
    public List<DiscountVariantResponseDto> getDiscountVariants(@PathVariable Long id) {
        List<DiscountVariant> discount = discountService.getVariantsByDiscountId(id);
        return discountVariantMapper.toDto(discount);
    }

    @GetMapping("/line-item/{order_id}")
    public DiscountLineItemResponseDto getDiscountLineItem(@PathVariable("order_id") Long orderId) {
        DiscountLineItem discount = discountLineItemService.findById(orderId);
        return discountLineItemMapper.toDto(discount);
    }
}
