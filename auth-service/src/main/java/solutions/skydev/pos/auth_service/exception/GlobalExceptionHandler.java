package solutions.skydev.pos.auth_service.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation Failed");
        problemDetail.setDetail(errorMessage);

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setDetail(ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred");

        return problemDetail;
    }

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleRuntimeException(RuntimeException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Runtime Exception");
        problemDetail.setDetail(ex.getMessage() != null ? ex.getMessage() : "A runtime error occurred");

        return problemDetail;
    }

    @ExceptionHandler(AuthExceptions.UserNotFoundException.class)
    public ProblemDetail handleUserNotFoundException(AuthExceptions.UserNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Account Not Found");
        problemDetail.setDetail(ex.getMessage() != null ? ex.getMessage() : "The requested user was not found");

        return problemDetail;
    }

    @ExceptionHandler(AuthExceptions.InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentialsException(AuthExceptions.InvalidCredentialsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Invalid Credentials");
        problemDetail.setDetail(ex.getMessage() != null ? ex.getMessage() : "The provided credentials are invalid");

        return problemDetail;
    }

    @ExceptionHandler(AuthExceptions.UserAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExistsException(AuthExceptions.UserAlreadyExistsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle("Account Already Exists");
        problemDetail.setDetail(ex.getMessage() != null ? ex.getMessage() : "A user with this username already exists");

        return problemDetail;
    }

    @ExceptionHandler(AuthExceptions.InvalidRefreshTokenException.class)
    public ProblemDetail handleInvalidRefreshTokenException(AuthExceptions.InvalidRefreshTokenException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Invalid Refresh Token");
        problemDetail.setDetail(ex.getMessage() != null ? ex.getMessage() : "The provided refresh token is invalid");

        return problemDetail;
    }

    @ExceptionHandler(AuthExceptions.RefreshTokenNotFoundException.class)
    public ProblemDetail handleRefreshTokenNotFoundException(AuthExceptions.RefreshTokenNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Refresh Token Not Found");
        problemDetail.setDetail(ex.getMessage() != null ? ex.getMessage() : "The requested refresh token was not found");

        return problemDetail;
    }

    @ExceptionHandler(AuthExceptions.RefreshTokenExpiredException.class)
    public ProblemDetail handleRefreshTokenExpiredException(AuthExceptions.RefreshTokenExpiredException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Refresh Token Expired");
        problemDetail.setDetail(ex.getMessage() != null ? ex.getMessage() : "The provided refresh token has expired");

        return problemDetail;
    }

}
