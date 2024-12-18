package com.ofg.auth.model.response;

import com.ofg.auth.model.entity.User;

import java.util.UUID;

public record UserWithoutRolesResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String profileImage) {
    public UserWithoutRolesResponse(User user) {
        this(user.getId(), user.getFirstName(),
                user.getLastName(), user.getEmail(),
                user.getProfileImage());
    }
}
