package solutions.skydev.pos.order_orchestrator_service.model.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PENDING("P"),
    COMPLETED("CO"),
    CANCELLED("CA");
    
    private final String code;
}
