package solutions.skydev.pos.order_orchestrator_service.service;

import solutions.skydev.pos.order_orchestrator_service.model.entity.Cart;

public interface CartService {
    Cart createCart(Cart cart);
    Cart getCartById(Long id);
    Cart updateCart(Cart cart);
    void deleteCart(Long id);
}
