package com.devblo.api.model.request.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterCustomerRequest(
                @NotBlank(message = "First name is required") String firstName,
                @NotBlank(message = "Last name is required") String lastName,
                @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
                @NotBlank(message = "Date of birth is required") String dateOfBirth,
                @NotBlank(message = "Street is required") String street,
                @NotBlank(message = "District is required") String district) {
}
