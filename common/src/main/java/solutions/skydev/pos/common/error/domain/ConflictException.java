package solutions.skydev.pos.common.error.domain;

import org.springframework.http.HttpStatus;
import solutions.skydev.pos.common.error.model.ErrorCode;

public class ConflictException extends DomainException {
    public ConflictException(String message) {
        super(message, ErrorCode.CONFLICT, HttpStatus.CONFLICT);
    }
    
    public ConflictException() {
        super(ErrorCode.CONFLICT.getMessage(), ErrorCode.CONFLICT, HttpStatus.CONFLICT);
    }
}
