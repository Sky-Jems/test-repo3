package solutions.skydev.pos.common.error.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import solutions.skydev.pos.common.error.domain.DomainException;

@Value
@Builder
@Jacksonized
@AllArgsConstructor
public class ErrorResponse {
    @JsonProperty("error_code")
    String errorCode;
    String message;
    
    public ErrorResponse(DomainException exception) {
        this.errorCode = exception.getErrorCode().getCode();
        this.message = exception.getMessage();
    }
}
