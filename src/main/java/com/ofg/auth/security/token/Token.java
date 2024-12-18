package com.ofg.auth.security.token;

import com.ofg.auth.core.model.entity.BaseEntity;
import com.ofg.auth.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token extends BaseEntity {
    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "prefix", nullable = false)
    private String prefix;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    public Token(String prefix, String token) {
        this.prefix = prefix;
        this.token = token;
    }
}