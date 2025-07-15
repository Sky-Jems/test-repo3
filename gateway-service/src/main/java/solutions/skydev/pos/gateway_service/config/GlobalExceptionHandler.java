package solutions.skydev.pos.gateway_service.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import solutions.skydev.pos.common.error.domain.DomainException;
import solutions.skydev.pos.common.error.domain.ServiceUnavailableException;
import solutions.skydev.pos.common.error.model.ErrorCode;
import solutions.skydev.pos.common.error.model.ErrorResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex);
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }
    
    @ExceptionHandler({ExecutionException.class, InterruptedException.class, TimeoutException.class})
    public ResponseEntity<ErrorResponse> handleServiceUnavailableException(Exception ex) {
        DomainException exception = new ServiceUnavailableException("Service is currently unavailable. Please try again later.");
        ErrorResponse error = new ErrorResponse(exception);
        return new ResponseEntity<>(error, exception.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = ErrorResponse.builder()
                .errorCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .message("An unexpected error occurred")
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
