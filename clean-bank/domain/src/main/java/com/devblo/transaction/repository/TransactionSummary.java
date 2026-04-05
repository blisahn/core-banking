package com.devblo.transaction.repository;

import com.devblo.account.AccountId;
import com.devblo.transaction.TransactionId;
import com.devblo.transaction.TransactionStatus;
import com.devblo.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionSummary(
        TransactionId id,
        AccountId sourceAccountId,
        AccountId targetAccountId,
        BigDecimal amount,
        String currency,
        TransactionType type,
        TransactionStatus status,
        String description,
        Instant timestamp
) {
}
