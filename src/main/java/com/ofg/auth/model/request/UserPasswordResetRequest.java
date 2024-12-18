package com.ofg.auth.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserPasswordResetRequest(
        @Email
        @NotBlank(message = "{app.constraint.email.not-blank}")
        String email) {

}