package solutions.skydev.pos.order_orchestrator_service.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
    PENDING("P"),
    REQUESTED("R"),
    APPROVED("A");

    private final String code;
}
