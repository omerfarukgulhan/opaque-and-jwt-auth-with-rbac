package com.ofg.auth.model.response;

import com.ofg.auth.security.token.Token;

public record TokenResponse(
        String prefix,
        String token) {
    public TokenResponse(Token token) {
        this(token.getPrefix(), token.getToken());
    }
}
