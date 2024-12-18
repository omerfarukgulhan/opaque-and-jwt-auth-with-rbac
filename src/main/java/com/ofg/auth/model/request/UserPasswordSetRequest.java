package com.ofg.auth.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserPasswordSetRequest(
        @Size(min = 8, max = 255)
        @NotBlank(message = "{app.constraint.password.not-blank}")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "{app.msg.constraint.password.pattern}")
        String newPassword) {

}