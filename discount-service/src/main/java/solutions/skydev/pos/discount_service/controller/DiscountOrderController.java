package solutions.skydev.pos.discount_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solutions.skydev.pos.common.discount_service.dto.response.DiscountOrderUpdatedResponseDto;
import solutions.skydev.pos.discount_service.model.DiscountOrderSummary;
import solutions.skydev.pos.discount_service.model.mapper.DiscountOrderMapper;
import solutions.skydev.pos.discount_service.service.DiscountOrderService;

@RestController
@RequestMapping("/discount-order")
public class DiscountOrderController {

    private final DiscountOrderService discountOrderService;
    private final DiscountOrderMapper discountOrderMapper;

    public DiscountOrderController(DiscountOrderService discountOrderService, DiscountOrderMapper discountOrderMapper) {
        this.discountOrderMapper = discountOrderMapper;
        this.discountOrderService = discountOrderService;
    }

    @GetMapping("/{order-id}")
    public DiscountOrderUpdatedResponseDto getDiscountLineItemsByOrderId(@PathVariable("order-id") Long orderId) {
        DiscountOrderSummary summary = discountOrderService.getDiscountOrderSummary(orderId);
        return discountOrderMapper.toDto(summary);
    }
}
