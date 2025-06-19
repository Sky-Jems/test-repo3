package solutions.skydev.pos.order_orchestrator_service.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import solutions.skydev.pos.order_orchestrator_service.model.enums.DiscountStatus;
import solutions.skydev.pos.order_orchestrator_service.model.enums.OrderStatus;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class DiscountStatusConverter implements AttributeConverter<DiscountStatus, String> {
    
    @Override
    public String convertToDatabaseColumn(DiscountStatus discountStatus) {
        return discountStatus == null ? null : discountStatus.getCode();
    }
    
    public DiscountStatus convertToEntityAttribute(String code) {
        return code == null ? null : Stream.of(DiscountStatus.values()).filter(discountStatus -> discountStatus.getCode().equals(code)).findFirst().orElse(null);
    }
}
