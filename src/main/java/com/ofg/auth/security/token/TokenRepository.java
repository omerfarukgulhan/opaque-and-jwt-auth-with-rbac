package com.ofg.auth.security.token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByToken(String token);

    Optional<Token> findByUserId(UUID userId);

    void deleteByUserId(UUID userId);
}