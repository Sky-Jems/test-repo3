package solutions.skydev.pos.order_orchestrator_service.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import solutions.skydev.pos.order_orchestrator_service.model.enums.PaymentStatus;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class PaymentStatusConverter implements AttributeConverter<PaymentStatus, String> {
    
    @Override
    public String convertToDatabaseColumn(PaymentStatus paymentStatus) {
        return paymentStatus == null ? null : paymentStatus.getCode();
    }
    
    public PaymentStatus convertToEntityAttribute(String code) {
        return code == null ? null : Stream.of(PaymentStatus.values()).filter(paymentStatus -> paymentStatus.getCode().equals(code)).findFirst().orElse(null);
    }
}
