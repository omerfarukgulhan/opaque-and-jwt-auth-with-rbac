package com.ofg.auth.security;

import java.io.IOException;

import com.ofg.auth.exception.authentication.TokenExpiredException;
import com.ofg.auth.model.entity.User;
import com.ofg.auth.security.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final HandlerExceptionResolver exceptionResolver;

    @Autowired
    public TokenFilter(TokenService tokenService,
                       @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.tokenService = tokenService;
        this.exceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String tokenWithPrefix = request.getHeader("Authorization");
        if (tokenWithPrefix != null) {
            try {
                User user = tokenService.verifyToken(tokenWithPrefix).orElse(null);
                if (user != null) {
                    if (!user.isActive()) {
                        exceptionResolver.resolveException(request, response, null, new DisabledException("User is disabled"));
                        return;
                    }
                    CurrentUser currentUser = new CurrentUser(user);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (TokenExpiredException e) {
                exceptionResolver.resolveException(request, response, null, new TokenExpiredException());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}