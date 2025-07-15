package solutions.skydev.pos.common.error.model;

import lombok.Getter;

@Getter
public enum ErrorCode {
    VALIDATION_ERROR("VALIDATION_ERROR", "Validation error occurred"),
    SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE", "Service is currently unavailable"),
    CONFLICT("CONFLICT", "Conflict occurred"),
    NOT_FOUND("NOT_FOUND", "Resource not found"),
    UNAUTHORIZED("UNAUTHORIZED", "Unauthorized access"),
    FORBIDDEN("FORBIDDEN", "Access forbidden"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "An internal server error occurred");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public static ErrorCode resolve(String code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return null; // or throw an exception if preferred
    }
}
