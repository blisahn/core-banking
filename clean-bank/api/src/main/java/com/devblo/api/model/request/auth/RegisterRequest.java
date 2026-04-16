package com.devblo.api.model.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "First name is required") String firstName,
        @NotBlank(message = "Last name is required") String lastName,
        @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
        @NotBlank(message = "Date of birth is required") String dateOfBirth,
        @NotBlank(message = "Street is required") String street,
        @NotBlank(message = "District is required") String district,
        @NotBlank(message = "Password is required") @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters") String password

) {
}
