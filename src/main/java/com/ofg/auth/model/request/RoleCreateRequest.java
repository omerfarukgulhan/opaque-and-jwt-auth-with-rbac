package com.ofg.auth.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleCreateRequest(
        @NotBlank(message = "{app.constraint.role-name.not-blank}")
        @Size(min = 3, max = 100, message = "{app.constraint.role-name.size}")
        String name) {

}