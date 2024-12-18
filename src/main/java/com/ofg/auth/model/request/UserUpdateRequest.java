package com.ofg.auth.model.request;

import com.ofg.auth.validation.FileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record UserUpdateRequest(
        @NotBlank(message = "{app.constraint.first-name.not-blank}")
        @Size(min = 3, max = 255)
        String firstName,
        @NotBlank(message = "{app.constraint.last-name.not-blank}")
        @Size(min = 3, max = 255)
        String lastName,
        @NotBlank(message = "{app.constraint.phone-number.not-blank}")
        @Size(min = 10, max = 15, message = "{app.constraint.phone-number.size}")
        String phoneNumber,
        @FileType(types = {"jpeg", "jpg", "png"})
        MultipartFile profileImage) {

}