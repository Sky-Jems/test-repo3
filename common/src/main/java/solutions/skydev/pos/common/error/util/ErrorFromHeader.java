package solutions.skydev.pos.common.error.util;

import org.apache.kafka.common.header.Headers;
import solutions.skydev.pos.common.error.domain.*;
import solutions.skydev.pos.common.error.model.ErrorCode;

public class ErrorFromHeader {
    
    private static final String ERROR_CODE_HEADER = "exception_error_code";
    private static final String ERROR_MESSAGE_HEADER = "exception_error_message";
    
    public static DomainException getDomainException(Headers headers) {
        String errorCode = new String(headers.lastHeader(ERROR_CODE_HEADER).value());
        String errorMessage = new String(headers.lastHeader(ERROR_MESSAGE_HEADER).value());
        
        return createDomainException(errorCode, errorMessage);
    }
    
    public static DomainException createDomainException(String errorCode, String errorMessage) {
        ErrorCode code = ErrorCode.resolve(errorCode);
        if (code == null) {
            throw new IllegalArgumentException("Invalid error code: " + errorCode);
        }
        
        switch (code) {
            case NOT_FOUND -> {
                return new ResourceNotFoundException(errorMessage);
            }
            case SERVICE_UNAVAILABLE -> {
                return new ServiceUnavailableException(errorMessage);
            }
            case CONFLICT ->  {
                return new ConflictException(errorMessage);
            }
            case VALIDATION_ERROR -> {
                return new ValidationException(errorMessage);
            }
        }
        
        throw new ServiceUnavailableException(errorMessage);
    }
}
