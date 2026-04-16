package com.devblo.api.model.request.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateStaffUserRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters") String password,
        @NotBlank String role
) {
}
