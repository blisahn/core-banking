package com.devblo.account.command.openAccount;

import com.devblo.account.AccountId;
import com.devblo.common.ICommand;
import com.devblo.common.result.Result;

import java.util.UUID;

/**
 * Command to open a new bank account.
 *
 * @param customerId  The customer who owns this account
 * @param accountType Type of account (CHECKING, SAVINGS, etc.)
 * @param currency    Currency code (e.g., "TRY", "USD", "EUR")
 */
public record OpenAccountCommand(
        UUID customerId,
        String  accountType,
        String currency
) implements ICommand<Result<AccountId>> {
}

