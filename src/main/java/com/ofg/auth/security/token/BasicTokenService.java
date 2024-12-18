package com.ofg.auth.security.token;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import com.ofg.auth.model.entity.User;
import com.ofg.auth.model.request.SignInCredentials;
import com.ofg.auth.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.token-type", havingValue = "basic")
public class BasicTokenService implements TokenService {
    private static final String BASIC = "Basic";

    private final UserService userService;

    @Autowired
    public BasicTokenService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Token createToken(User user, SignInCredentials signInCredentials) {
        String email = signInCredentials.email();
        String randomValue = UUID.randomUUID().toString();
        String tokenData = email + ":" + randomValue;
        String token = Base64.getEncoder().encodeToString(tokenData.getBytes());
        return new Token(BASIC, token);
    }

    @Override
    public Optional<User> verifyToken(String authorizationHeader) {
        String base64Encoded = extractToken(authorizationHeader);
        if (base64Encoded == null) return Optional.empty();

        var decoded = new String(Base64.getDecoder().decode(base64Encoded));
        var credentials = decoded.split(":");
        if (credentials.length != 2) return Optional.empty();

        var email = credentials[0];

        User user = userService.getUserByEmail(email);
        if (user == null) return Optional.empty();

        return Optional.of(user);
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            return authorizationHeader.substring(6);
        }
        return null;
    }

    @Override
    public void logout(UUID userId) {
    }
}
