package solutions.skydev.pos.common.error.domain;

import org.springframework.http.HttpStatus;
import solutions.skydev.pos.common.error.model.ErrorCode;

public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String message) {
        super(message, ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    
    public ResourceNotFoundException() {
        super(ErrorCode.NOT_FOUND.getMessage(), ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
