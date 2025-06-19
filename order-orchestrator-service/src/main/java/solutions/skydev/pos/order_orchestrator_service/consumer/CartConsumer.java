package solutions.skydev.pos.order_orchestrator_service.consumer;

import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import solutions.skydev.pos.order_orchestrator_service.model.entity.Cart;
import solutions.skydev.pos.order_orchestrator_service.model.mapper.CartMapper;
import solutions.skydev.pos.order_orchestrator_service.service.CartService;

@Component
public class CartConsumer {
    private final CartService cartService;
    private final CartMapper cartMapper;

    public CartConsumer(CartService cartService, CartMapper cartMapper) {
        this.cartService = cartService;
        this.cartMapper = cartMapper;
    }

    @KafkaListener(topics = "create-cart-command")
    @AsyncListener(operation = @AsyncOperation(
            channelName = "create-cart-command",
            description = "Create cart command",
            payloadType = Cart.class
    ))
    @KafkaAsyncOperationBinding
    @SendTo("cart.created")
    public void createCartCommand(ConsumerRecord<String, String> record) {
        System.out.println("Received create cart command: " + record.value());
        // received record is Order entity, we need to create a cart from it
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        CartRequestDto cartRequestDto = null;
//        try {
//            cartRequestDto = mapper.readValue(record.value(), CartRequestDto.class);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to deserialize CartRequestDto", e);
//        }
//        Cart cart = cartMapper.toEntity(cartRequestDto);
//        cartService.createCart(cart);
    }
}
