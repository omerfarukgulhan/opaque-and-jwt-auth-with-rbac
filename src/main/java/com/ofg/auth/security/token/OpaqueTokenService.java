package com.ofg.auth.security.token;

import com.ofg.auth.exception.authentication.TokenExpiredException;
import com.ofg.auth.model.entity.User;
import com.ofg.auth.model.request.SignInCredentials;
import com.ofg.auth.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "app.token-type", havingValue = "opaque")
public class OpaqueTokenService implements TokenService {
    private static final String BEARER = "Bearer";

    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final long expirationTime;

    @Autowired
    public OpaqueTokenService(TokenRepository tokenRepository,
                              UserService userService,
                              @Value("${app.token.expiration-time}") long expirationTime) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
        this.expirationTime = expirationTime;
    }

    @Transactional
    @Override
    public Token createToken(User user, SignInCredentials signInCredentials) {
        Optional<Token> existingToken = tokenRepository.findByUserId(user.getId());
        existingToken.ifPresent(token -> tokenRepository.deleteByUserId(token.getUser().getId()));

        String tokenString = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plusMillis(expirationTime);
        Token token = new Token(tokenString, BEARER, user, expiresAt);
        tokenRepository.save(token);

        return token;
    }

    @Override
    public Optional<User> verifyToken(String authorizationHeader) {
        return extractToken(authorizationHeader)
                .flatMap(token -> {
                    Token tokenInDB = tokenRepository.findByToken(token).orElse(null);

                    if (tokenInDB == null) {
                        return Optional.empty();
                    }
                    if (tokenInDB.getExpiresAt().isBefore(Instant.now())) {
                        throw new TokenExpiredException();
                    }

                    return Optional.ofNullable(userService.getUserEntityById(tokenInDB.getUser().getId()));
                });
    }

    @Transactional
    @Override
    public void logout(UUID userId) {
        tokenRepository.deleteByUserId(userId);
    }

    private Optional<String> extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER + " ")) {
            return Optional.of(authorizationHeader.substring(BEARER.length() + 1));
        }
        return Optional.empty();
    }
}