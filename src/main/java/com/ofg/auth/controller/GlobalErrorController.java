package com.ofg.auth.controller;

import com.ofg.auth.core.util.results.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalErrorController extends ResponseEntityExceptionHandler {
    @ExceptionHandler({DisabledException.class, AccessDeniedException.class})
    public ResponseEntity<?> handleDisabledException(Exception exception, HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setMessage(exception.getMessage());
        apiErrorResponse.setPath(request.getRequestURI());
        if (exception instanceof DisabledException) {
            apiErrorResponse.setStatus(401);
        } else if (exception instanceof AccessDeniedException) {
            apiErrorResponse.setStatus(403);
        }
        return ResponseEntity.status(apiErrorResponse.getStatus()).body(apiErrorResponse);
    }
}