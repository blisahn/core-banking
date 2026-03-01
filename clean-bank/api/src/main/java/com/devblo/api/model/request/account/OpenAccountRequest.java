package com.devblo.api.model.request.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record OpenAccountRequest(
        @NotNull(message = "Customer ID is required") UUID customerId,

        @NotBlank(message = "Account type is required") String accountType,

        @NotBlank(message = "Currency is required") @Size(min = 3, max = 3, message = "Currency must be 3 characters") String currency) {
}
