package solutions.skydev.pos.billing_service.model.mapper;

import org.mapstruct.Mapper;
import solutions.skydev.pos.billing_service.model.entity.BillingRequest;
import solutions.skydev.pos.billing_service.model.dto.request.BillingRequestRequestDto;
import solutions.skydev.pos.billing_service.model.dto.response.BillingRequestResponseDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BillingRequestMapper {
    BillingRequestRequestDto toDto(BillingRequest billingRequest);
    BillingRequest toEntity(BillingRequestRequestDto dto);
    BillingRequestResponseDto toResponseDto(BillingRequest billingRequest);
    List<BillingRequestResponseDto> toResponseDtoList(List<BillingRequest> billingRequests);
}
