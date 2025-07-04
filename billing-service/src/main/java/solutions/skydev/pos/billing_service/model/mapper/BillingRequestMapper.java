package solutions.skydev.pos.billing_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import solutions.skydev.pos.billing_service.model.entity.BillingRequest;
import solutions.skydev.pos.common.billing_service.dto.request.BillingRequestRequestDto;
import solutions.skydev.pos.common.billing_service.dto.response.BillingRequestResponseDto;

import java.lang.annotation.Target;
import java.lang.classfile.instruction.NewMultiArrayInstruction;
import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BillingRequestMapper {
    BillingRequestRequestDto toDto(BillingRequest billingRequest);
    BillingRequest toEntity(BillingRequestRequestDto dto);
    BillingRequestResponseDto toResponseDto(BillingRequest billingRequest);
    List<BillingRequestResponseDto> toResponseDtoList(List<BillingRequest> billingRequests);
}
