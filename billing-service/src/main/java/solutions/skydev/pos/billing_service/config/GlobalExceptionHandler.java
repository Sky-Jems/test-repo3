package solutions.skydev.pos.billing_service.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import solutions.skydev.pos.common.config.BaseGlobalExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseGlobalExceptionHandler {
    // All exception handling is provided by BaseGlobalExceptionHandler
}