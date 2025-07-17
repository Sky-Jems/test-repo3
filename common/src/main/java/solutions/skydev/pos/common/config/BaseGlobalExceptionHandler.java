package solutions.skydev.pos.common.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import solutions.skydev.pos.common.error.domain.DomainException;
import solutions.skydev.pos.common.error.domain.ServiceUnavailableException;
import solutions.skydev.pos.common.error.model.ErrorCode;
import solutions.skydev.pos.common.error.model.ErrorResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Base exception handler that provides common exception handling for all services.
 * Services should extend this class and add their specific exception handlers if needed.
 */
public abstract class BaseGlobalExceptionHandler {
    
    /**
     * Handles DomainException and its subclasses.
     * @param ex the exception
     * @return a ResponseEntity with an ErrorResponse
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex);
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }
    
    /**
     * Handles service unavailability exceptions (ExecutionException, InterruptedException, TimeoutException).
     * @param ex the exception
     * @return a ResponseEntity with an ErrorResponse
     */
    @ExceptionHandler({ExecutionException.class, InterruptedException.class, TimeoutException.class})
    public ResponseEntity<ErrorResponse> handleServiceUnavailableException(Exception ex) {
        DomainException exception = new ServiceUnavailableException("Service is currently unavailable. Please try again later.");
        ErrorResponse error = new ErrorResponse(exception);
        return new ResponseEntity<>(error, exception.getHttpStatus());
    }

    /**
     * Handles generic exceptions.
     * @param ex the exception
     * @return a ResponseEntity with an ErrorResponse
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = ErrorResponse.builder()
                .errorCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .message("An unexpected error occurred")
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}