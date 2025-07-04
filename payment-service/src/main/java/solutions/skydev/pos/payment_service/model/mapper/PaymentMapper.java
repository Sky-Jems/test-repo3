package solutions.skydev.pos.payment_service.model.mapper;

import org.mapstruct.Mapper;
import solutions.skydev.pos.common.payment_service.dto.request.PaymentRequestDto;
import solutions.skydev.pos.common.payment_service.dto.response.PaymentResponseDto;
import solutions.skydev.pos.payment_service.model.entity.Payment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentRequestDto toDto(Payment payment);
    Payment toEntity(PaymentRequestDto paymentRequestDto);
    PaymentResponseDto toResponseDto(Payment payment);
    List<PaymentResponseDto> toResponseDto(List<Payment> payments);
}
