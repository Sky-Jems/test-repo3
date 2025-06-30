package solutions.skydev.pos.discount_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import solutions.skydev.pos.common.discount_service.dto.request.DiscountRequestDto;
import solutions.skydev.pos.common.discount_service.dto.response.DiscountResponseDto;
import solutions.skydev.pos.discount_service.model.entity.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DiscountMapper {
    Discount toEntity(DiscountRequestDto dto);

    default LocalDateTime map(String value) {
        return value != null ? LocalDateTime.parse(value) : null;
    }

    List<DiscountResponseDto> toDto(List<Discount> discounts);

    DiscountResponseDto toDto(Discount discounts);
    default String map(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toString() : null;
    }
}
