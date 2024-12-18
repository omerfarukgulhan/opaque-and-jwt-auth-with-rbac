package com.ofg.auth.model.response;

import com.ofg.auth.model.entity.User;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String profileImage,
        Set<RoleResponse> roles) {
    public UserResponse(User user) {
        this(user.getId(), user.getEmail(), user.getFirstName(),
                user.getLastName(), user.getProfileImage(),
                user.getRoles().stream().map(RoleResponse::new).collect(Collectors.toSet()));
    }
}