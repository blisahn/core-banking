package com.devblo.api.model.request.customer;

import jakarta.validation.constraints.NotBlank;

public record UpdateAddressRequest(
        @NotBlank String street,
        @NotBlank String district) {
}
