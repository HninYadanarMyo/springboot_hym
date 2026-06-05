package com.talent.java.batch11.springbootapp.exception;

import com.talent.java.batch11.springbootapp.controller.AccountRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(assignableTypes = {AccountRestController.class})
public class AccountGlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleAccountExceptions(RuntimeException ex) {
        Map<String, String> errorResponse = new HashMap<>();

        if ("Incorrect password".equals(ex.getMessage())) {
            errorResponse.put("status", "fail");
            errorResponse.put("message", "Incorrect password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        errorResponse.put("status", "fail");
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.noContent().build();
    }
}