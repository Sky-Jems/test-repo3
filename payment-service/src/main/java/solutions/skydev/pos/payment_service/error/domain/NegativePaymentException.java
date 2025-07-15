package solutions.skydev.pos.payment_service.error.domain;

import solutions.skydev.pos.common.error.domain.ValidationException;

public class NegativePaymentException extends ValidationException {
    public NegativePaymentException() {
        super("Payment amount cannot be negative.");
    }
}
