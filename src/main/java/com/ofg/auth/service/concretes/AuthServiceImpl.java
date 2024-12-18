package com.ofg.auth.service.concretes;

import com.ofg.auth.exception.authentication.AuthenticationException;
import com.ofg.auth.exception.authentication.UserInactiveException;
import com.ofg.auth.model.entity.User;
import com.ofg.auth.model.request.SignInCredentials;
import com.ofg.auth.model.request.UserCreateRequest;
import com.ofg.auth.model.response.AuthResponse;
import com.ofg.auth.model.response.UserResponse;
import com.ofg.auth.security.token.Token;
import com.ofg.auth.security.token.TokenService;
import com.ofg.auth.service.abstracts.AuthService;
import com.ofg.auth.service.abstracts.UserService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Autowired
    public AuthServiceImpl(UserService userService,
                           PasswordEncoder passwordEncoder,
                           TokenService tokenService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public AuthResponse login(SignInCredentials signInCredentials) {
        User user = userService.getUserByEmail(signInCredentials.email());

        validateUser(user, signInCredentials.password());

        Token token = tokenService.createToken(user, signInCredentials);
        return new AuthResponse(user, token);
    }

    private void validateUser(User user, String rawPassword) {
        if (!user.isActive()) {
            throw new UserInactiveException();
        }
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new AuthenticationException();
        }
    }

    @Override
    public UserResponse register(UserCreateRequest userCreateRequest) {
        return userService.addUser(userCreateRequest);
    }

    @Override
    public void logout(UUID userId) {
        tokenService.logout(userId);
    }
}
