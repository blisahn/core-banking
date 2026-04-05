package com.devblo.api.model.request.account;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank(message = "Target IBAN is required") String targetAccountNumber,

        @NotNull(message = "Amount is required") @DecimalMin(value = "0.01", message = "Amount must be positive") BigDecimal amount,

        @NotBlank(message = "Currency is required") @Size(min = 3, max = 3, message = "Currency must be 3 characters") String currency,

        @NotBlank(message = "Description is required") String description) {
}
