package com.devblo.api.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositTransactionRequest(
        @NotBlank(message = "Account ID cannot be empty")
        String targetAccountId,
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        BigDecimal amount,
        @NotBlank(message = "Currency type is required")
        String currency,
        @NotBlank(message = "Description is required")
        String description
) {
}
