package com.ofg.auth.model.response;

import com.ofg.auth.model.entity.User;
import com.ofg.auth.security.token.Token;

public record AuthResponse(
        UserResponse user,
        TokenResponse token) {
    public AuthResponse(User user, Token token) {
        this(new UserResponse(user), new TokenResponse(token));
    }
}
