package com.ofg.auth.controller;

import java.util.stream.Collectors;

import com.ofg.auth.core.util.message.Messages;
import com.ofg.auth.core.util.results.ApiErrorResponse;
import com.ofg.auth.exception.authentication.*;
import com.ofg.auth.exception.email.ActivationNotificationException;
import com.ofg.auth.exception.email.EmailServiceException;
import com.ofg.auth.exception.file.FileServiceException;
import com.ofg.auth.exception.general.NotFoundException;
import com.ofg.auth.exception.general.NotUniqueEmailException;
import com.ofg.auth.exception.general.UniqueConstraintViolationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler({
            ActivationNotificationException.class,
            AuthenticationException.class,
            EmailServiceException.class,
            FileServiceException.class,
            InvalidPasswordException.class,
            InvalidTokenException.class,
            MethodArgumentNotValidException.class,
            MissingAuthorizationHeaderException.class,
            NotFoundException.class,
            NotUniqueEmailException.class,
            TokenExpiredException.class,
            UniqueConstraintViolationException.class,
            UserInactiveException.class,
            UnauthorizedException.class,
    })
    public ResponseEntity<ApiErrorResponse> handleException(Exception exception, HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setPath(request.getRequestURI());
        apiErrorResponse.setMessage(exception.getMessage());

        int status = 500;
        if (exception instanceof ActivationNotificationException) {
            status = 502;
        } else if (exception instanceof AuthenticationException) {
            status = 401;
        } else if (exception instanceof EmailServiceException) {
            status = 500;
        } else if (exception instanceof FileServiceException) {
            status = 500;
        } else if (exception instanceof InvalidPasswordException) {
            status = 400;
        } else if (exception instanceof InvalidTokenException) {
            status = 400;
        } else if (exception instanceof MethodArgumentNotValidException) {
            String message = Messages.getMessageForLocale("app.msg.error.validation", LocaleContextHolder.getLocale());
            apiErrorResponse.setMessage(message);
            status = 400;
            var validationErrors = ((MethodArgumentNotValidException) exception)
                    .getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (existing, replacing) -> existing));
            apiErrorResponse.setValidationErrors(validationErrors);
        } else if (exception instanceof MissingAuthorizationHeaderException) {
            status = 401;
        } else if (exception instanceof NotFoundException) {
            status = 404;
        } else if (exception instanceof NotUniqueEmailException) {
            status = 400;
            apiErrorResponse.setValidationErrors(((NotUniqueEmailException) exception).getValidationErrors());
        } else if (exception instanceof TokenExpiredException) {
            status = 401;
        } else if (exception instanceof UniqueConstraintViolationException) {
            status = 409;
        } else if (exception instanceof UserInactiveException) {
            status = 403;
        } else if (exception instanceof UnauthorizedException) {
            status = 401;
        }

        apiErrorResponse.setStatus(status);
        return ResponseEntity.status(status).body(apiErrorResponse);
    }
}
