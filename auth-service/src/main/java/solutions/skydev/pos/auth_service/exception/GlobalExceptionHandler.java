package solutions.skydev.pos.auth_service.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import solutions.skydev.pos.common.config.BaseGlobalExceptionHandler;
import solutions.skydev.pos.common.error.model.ErrorCode;
import solutions.skydev.pos.common.error.model.ErrorResponse;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseGlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.VALIDATION_ERROR.getCode())
                .message(errorMessage)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AuthExceptions.UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(AuthExceptions.UserNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.NOT_FOUND.getCode())
                .message(ex.getMessage() != null ? ex.getMessage() : "The requested user was not found")
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(AuthExceptions.InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(AuthExceptions.InvalidCredentialsException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.UNAUTHORIZED.getCode())
                .message(ex.getMessage() != null ? ex.getMessage() : "The provided credentials are invalid")
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(AuthExceptions.UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(AuthExceptions.UserAlreadyExistsException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.CONFLICT.getCode())
                .message(ex.getMessage() != null ? ex.getMessage() : "A user with this username already exists")
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(AuthExceptions.InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRefreshTokenException(AuthExceptions.InvalidRefreshTokenException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.UNAUTHORIZED.getCode())
                .message(ex.getMessage() != null ? ex.getMessage() : "The provided refresh token is invalid")
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(AuthExceptions.RefreshTokenNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRefreshTokenNotFoundException(AuthExceptions.RefreshTokenNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.NOT_FOUND.getCode())
                .message(ex.getMessage() != null ? ex.getMessage() : "The requested refresh token was not found")
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(AuthExceptions.RefreshTokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleRefreshTokenExpiredException(AuthExceptions.RefreshTokenExpiredException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.UNAUTHORIZED.getCode())
                .message(ex.getMessage() != null ? ex.getMessage() : "The provided refresh token has expired")
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}
