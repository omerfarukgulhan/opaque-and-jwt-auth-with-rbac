package com.ofg.auth.security;

import java.io.IOException;

import com.ofg.auth.core.util.results.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        apiErrorResponse.setMessage("You do not have permission to access this resource");
        apiErrorResponse.setPath(request.getRequestURI());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        objectMapper.writeValue(response.getOutputStream(), apiErrorResponse);
    }
}