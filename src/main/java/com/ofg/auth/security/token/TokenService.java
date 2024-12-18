package com.ofg.auth.security.token;

import com.ofg.auth.model.entity.User;
import com.ofg.auth.model.request.SignInCredentials;

import java.util.Optional;
import java.util.UUID;

public interface TokenService {
    Token createToken(User user, SignInCredentials signInCredentials);

    Optional<User> verifyToken(String authorizationHeader);

    void logout(UUID userId);
}