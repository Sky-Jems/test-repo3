package solutions.skydev.pos.common.error.domain;

import org.springframework.http.HttpStatus;
import solutions.skydev.pos.common.error.model.ErrorCode;

public class ValidationException extends DomainException {
    public ValidationException(String message) {
        super(message, ErrorCode.VALIDATION_ERROR, HttpStatus.BAD_REQUEST);
    }
    
    public ValidationException() {
        super(ErrorCode.VALIDATION_ERROR.getMessage(), ErrorCode.VALIDATION_ERROR, HttpStatus.BAD_REQUEST);
    }
}
