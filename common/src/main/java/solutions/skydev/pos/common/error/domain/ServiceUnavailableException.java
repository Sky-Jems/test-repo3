package solutions.skydev.pos.common.error.domain;

import org.springframework.http.HttpStatus;
import solutions.skydev.pos.common.error.model.ErrorCode;

public class ServiceUnavailableException extends DomainException {
    public ServiceUnavailableException(String message) {
        super(message, ErrorCode.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE);
    }
    
    public ServiceUnavailableException() {
        super(ErrorCode.SERVICE_UNAVAILABLE.getMessage(), ErrorCode.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
