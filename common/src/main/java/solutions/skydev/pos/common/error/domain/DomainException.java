package solutions.skydev.pos.common.error.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import solutions.skydev.pos.common.error.model.ErrorCode;

@AllArgsConstructor
@Getter
@Setter
public abstract class DomainException extends RuntimeException {
    private final String message;
    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
}