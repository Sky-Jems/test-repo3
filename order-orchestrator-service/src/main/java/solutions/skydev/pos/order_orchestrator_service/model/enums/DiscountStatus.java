package solutions.skydev.pos.order_orchestrator_service.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiscountStatus {
    REQUESTED("R"),
    PENDING("P"),
    SUCCEEDED("S");
    
    public final String code;
}
