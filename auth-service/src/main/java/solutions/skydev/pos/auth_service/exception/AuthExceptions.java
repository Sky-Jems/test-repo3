package solutions.skydev.pos.auth_service.exception;

public class AuthExceptions {

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }

        public UserNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException(String message) {
            super(message);
        }

        public InvalidCredentialsException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }

        public UserAlreadyExistsException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InvalidRefreshTokenException extends RuntimeException {
        public InvalidRefreshTokenException(String message) {
            super(message);
        }

        public InvalidRefreshTokenException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class RefreshTokenNotFoundException extends RuntimeException {
        public RefreshTokenNotFoundException(String message) {
            super(message);
        }

        public RefreshTokenNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class RefreshTokenExpiredException extends RuntimeException {
        public RefreshTokenExpiredException(String message) {
            super(message);
        }

        public RefreshTokenExpiredException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
