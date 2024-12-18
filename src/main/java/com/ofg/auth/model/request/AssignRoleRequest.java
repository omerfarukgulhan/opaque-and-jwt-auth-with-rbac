package com.ofg.auth.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AssignRoleRequest(
        @NotNull(message = "{app.constraint.user-id.not-null}")
        UUID userId,
        @NotBlank(message = "{app.constraint.role-name.not-blank}")
        @Size(min = 3, max = 100, message = "{app.constraint.role-name.size}")
        String roleName) {

}