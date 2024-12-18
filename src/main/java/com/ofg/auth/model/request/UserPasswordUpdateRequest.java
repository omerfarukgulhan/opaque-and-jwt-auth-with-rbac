package com.ofg.auth.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserPasswordUpdateRequest(
        @NotBlank(message = "{app.constraint.password.not-blank}")
        String oldPassword,
        @NotBlank(message = "{app.constraint.password.not-blank}")
        @Size(min = 8, max = 255)
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "{app.msg.constraint.password.pattern}")
        String newPassword) {

}