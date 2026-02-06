package com.devblo.account.repository;

import com.devblo.account.AccountId;
import com.devblo.account.AccountStatus;
import com.devblo.account.AccountType;
import com.devblo.shared.Money;

import java.time.Instant;

/**
 *
 * @param id
 * @param accountNumber
 * @param balance
 * @param customerName
 * @param type
 * @param status
 * @param createdAt
 * Read-Only AccountDto for CQRS -> Implementation for Read
 */
public record AccountSummary(
        AccountId id,
        String accountNumber,
        Money balance,
        String customerName,      // with Join
        AccountType type,
        AccountStatus status,
        Instant createdAt
) {

}