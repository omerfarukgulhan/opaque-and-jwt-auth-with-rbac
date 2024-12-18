package com.ofg.auth.service.abstracts;

import com.ofg.auth.model.request.SignInCredentials;
import com.ofg.auth.model.request.UserCreateRequest;
import com.ofg.auth.model.response.AuthResponse;
import com.ofg.auth.model.response.UserResponse;

import java.util.UUID;

public interface AuthService {
    AuthResponse login(SignInCredentials signInCredentials);

    UserResponse register(UserCreateRequest userCreateRequest);

    void logout(UUID userId);
}
