package com.ofg.auth.model.request;

import com.ofg.auth.model.entity.User;
import com.ofg.auth.validation.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank(message = "{app.constraint.email.not-blank}")
        @Email
        @UniqueEmail
        String email,
        @NotBlank(message = "{app.constraint.first-name.not-blank}")
        @Size(min = 3, max = 255)
        String firstName,
        @NotBlank(message = "{app.constraint.last-name.not-blank}")
        @Size(min = 3, max = 255)
        String lastName,
        @NotBlank(message = "{app.constraint.phone-number.not-blank}")
        @Size(min = 10, max = 15, message = "{app.constraint.phone-number.size}")
        String phoneNumber,
        @Size(min = 8, max = 255)
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "{app.constraint.password.pattern}")
        String password) {
    public User toUser() {
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        return user;
    }
}