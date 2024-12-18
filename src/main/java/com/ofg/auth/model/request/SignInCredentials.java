package com.ofg.auth.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignInCredentials(
        @Email
        @NotBlank(message = "{app.constraint.email.not-blank}")
        String email,
        @NotBlank(message = "{app.constraint.password.not-blank}")
        String password) {

}