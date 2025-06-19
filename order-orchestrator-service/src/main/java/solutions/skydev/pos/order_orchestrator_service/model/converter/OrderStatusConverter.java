package solutions.skydev.pos.order_orchestrator_service.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import solutions.skydev.pos.order_orchestrator_service.model.enums.OrderStatus;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {
    
    @Override
    public String convertToDatabaseColumn(OrderStatus orderStatus) {
        return orderStatus == null ? null : orderStatus.getCode();
    }
    
    public OrderStatus convertToEntityAttribute(String code) {
        return code == null ? null : Stream.of(OrderStatus.values()).filter(orderStatus -> orderStatus.getCode().equals(code)).findFirst().orElse(null);
    }
}
