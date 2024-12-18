package com.ofg.auth.controller;

import com.ofg.auth.core.util.response.ResponseUtil;
import com.ofg.auth.core.util.results.ApiDataResponse;
import com.ofg.auth.core.util.results.ApiResponse;
import com.ofg.auth.model.request.SignInCredentials;
import com.ofg.auth.model.request.UserCreateRequest;
import com.ofg.auth.model.response.AuthResponse;
import com.ofg.auth.model.response.UserResponse;
import com.ofg.auth.security.CurrentUser;
import com.ofg.auth.service.abstracts.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthService authService;
    private final String tokenType;

    private static final String LOGIN_SUCCESS = "app.msg.login.success";
    private static final String REGISTER_SUCCESS = "app.msg.register.success";
    private static final String LOGOUT_SUCCESS = "app.msg.logout.success";
    private static final String ENDPOINT_DISABLED = "app.msg.endpoint.disabled";

    @Autowired
    public AuthController(AuthService authService,
                          @Value("${app.token-type}") String tokenType) {
        this.authService = authService;
        this.tokenType = tokenType;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiDataResponse<AuthResponse>> login(@Valid @RequestBody SignInCredentials signInCredentials) {
        AuthResponse authResponse = authService.login(signInCredentials);
        return ResponseUtil.createApiDataResponse(authResponse, LOGIN_SUCCESS, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiDataResponse<UserResponse>> register(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        UserResponse userResponse = authService.register(userCreateRequest);
        return ResponseUtil.createApiDataResponse(userResponse, REGISTER_SUCCESS, HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@AuthenticationPrincipal CurrentUser currentUser) {
        if ("opaque".equals(tokenType)) {
            authService.logout(currentUser.getId());
            return ResponseUtil.createApiResponse(LOGOUT_SUCCESS, HttpStatus.NO_CONTENT);
        }
        return ResponseUtil.createApiResponse(false, ENDPOINT_DISABLED, HttpStatus.FORBIDDEN);
    }
}
