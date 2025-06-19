package solutions.skydev.pos.payment_service.model.mapper;

import org.mapstruct.Mapper;
import solutions.skydev.pos.payment_service.model.dto.request.PaymentRequestDto;
import solutions.skydev.pos.payment_service.model.dto.response.PaymentResponseDto;
import solutions.skydev.pos.payment_service.model.entity.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentRequestDto toDto(Payment payment);
    Payment toEntity(PaymentRequestDto paymentRequestDto);
    PaymentResponseDto toResponseDto(Payment payment);
}
