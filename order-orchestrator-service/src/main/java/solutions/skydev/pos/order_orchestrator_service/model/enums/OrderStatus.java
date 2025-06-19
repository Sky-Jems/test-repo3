package solutions.skydev.pos.order_orchestrator_service.model.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    CREATE_REQUESTED("CR"),
    PENDING("P"),
    UPDATE_REQUESTED("UR"),
    COMPLETED("CO"),
    CANCELLED("CA");
    
    private final String code;
}
