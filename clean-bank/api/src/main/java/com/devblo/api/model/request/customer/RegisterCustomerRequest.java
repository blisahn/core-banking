package com.devblo.api.model.request.customer;

import jakarta.validation.constraints.NotBlank;

public record RegisterCustomerRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        String email,
        @NotBlank
        String dateOfBirth,
        @NotBlank
        String street,
        @NotBlank
        String district) {
}
