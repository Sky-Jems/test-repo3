package solutions.skydev.pos.order_orchestrator_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solutions.skydev.pos.order_orchestrator_service.model.entity.Cart;
import solutions.skydev.pos.order_orchestrator_service.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository) { this.cartRepository = cartRepository; }

    public Cart createCart(Cart cart) { return cartRepository.save(cart); }

    public Cart getCartById(Long id) { return cartRepository.findById(id).orElse(null); }

    public Cart updateCart(Cart cart) { return cartRepository.save(cart); }

    public void deleteCart(Long id) { cartRepository.deleteById(id); }
}
