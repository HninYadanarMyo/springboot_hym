package com.talent.java.batch11.springbootapp.exception;

import com.talent.java.batch11.springbootapp.controller.AccountRestController;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(assignableTypes = {AccountRestController.class})
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<CommonResponse> error(String apiName, HttpStatus status, String message) {
        return ResponseUtils.makeCommonResponse(apiName, status, null, Boolean.FALSE, message);
    }

    private String getOriginMethod(Throwable ex) {
        String basePackage = "com.talent.java.batch11.springbootapp";
        for (StackTraceElement element : ex.getStackTrace()) {
            String className = element.getClassName();
            if (className.startsWith(basePackage)) {
                return element.getClassName() + "." + element.getMethodName() + " (Line:" + element.getLineNumber() + ")";
            }
        }
        for (StackTraceElement element : ex.getStackTrace()) {
            String className = element.getClassName();
            if (!className.startsWith("java.") && !className.startsWith("jdk.") &&
                    !className.startsWith("sun.") && !className.contains("Proxy")) {
                return element.getClassName() + "." + element.getMethodName() + " (Line:" + element.getLineNumber() + ")";
            }
        }
        return ex.getStackTrace().length > 0 ? ex.getStackTrace()[0].toString() : "Unknown Source";
    }

    // ========================= 400 Validation Error =========================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String method = getOriginMethod(ex);
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request parameters");
        logger.warn("Validation failed at {}: {}", method, message);

        return error("validation-error", HttpStatus.BAD_REQUEST, message);
    }

    // ========================= 401 Unauthorized =========================
    @ExceptionHandler({ AuthenticationException.class, BadCredentialsException.class })
    public ResponseEntity<CommonResponse> handleUnauthorizedException(Exception ex) {
        String method = getOriginMethod(ex);
        logger.warn("Unauthorized access at {}: {}", method, ex.getMessage());

        return error("unauthorized-access", HttpStatus.UNAUTHORIZED, "Unauthorized access. Please provide valid credentials.");
    }

    // ========================= 403 Forbidden =========================
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CommonResponse> handleAccessDenied(AccessDeniedException ex) {
        String method = getOriginMethod(ex);
        logger.warn("Access denied at {}: {}", method, ex.getMessage());

        return error("access-denied", HttpStatus.FORBIDDEN, "You do not have permission to access this resource.");
    }

    // ========================= Null Pointer =========================
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<CommonResponse> handleNullPointerException(NullPointerException ex) {
        String method = getOriginMethod(ex);
        logger.error("Null pointer exception at {}: {}", method, ex.getMessage());

        return error("null-pointer-error", HttpStatus.EXPECTATION_FAILED, ex.getMessage() != null ? ex.getMessage() : "A null value was encountered");
    }

    // ========================= 💡 ResponseStatusException (Service မှ တက်လာသမျှ) =========================
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CommonResponse> handleResponseStatusException(ResponseStatusException ex) {
        String method = getOriginMethod(ex);
        logger.warn("ResponseStatusException at {}: {} ({})", method, ex.getReason(), ex.getStatusCode());

        String message = ex.getReason() != null ? ex.getReason() : "Request failed";
        HttpStatusCode statusCode = ex.getStatusCode();
        HttpStatus status = HttpStatus.resolve(statusCode.value());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return error("api-error", status, message);
    }

    // ========================= 500 General Exception =========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> handleGlobalException(Exception ex) {
        String method = getOriginMethod(ex);
        logger.error("Unhandled exception at {}: {}", method, ex.getMessage());

        return error("internal-server-error", HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please contact support.");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<CommonResponse> handleNoResourceFound(NoResourceFoundException ex) {
        String method = getOriginMethod(ex);
        logger.warn("No handler/resource found at {}: {}", method, ex.getMessage());

        return error("resource-not-found", HttpStatus.NOT_FOUND, "The requested endpoint was not found.");
    }

    // ========================= JWT Expired =========================
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<CommonResponse> handleExpiredJwt(ExpiredJwtException ex) {
        String method = getOriginMethod(ex);
        logger.warn("JWT token expired at {}: {}", method, ex.getMessage());
        return error("jwt-expired", HttpStatus.UNAUTHORIZED, "Your session has expired. Please login again.");
    }

    // ========================= General JWT Exception =========================
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<CommonResponse> handleJwtException(JwtException ex) {
        String method = getOriginMethod(ex);
        logger.warn("Invalid JWT token at {}: {}", method, ex.getMessage());

        return error("jwt-invalid", HttpStatus.UNAUTHORIZED, "Invalid authentication token. Please login again.");
    }
}

