package com.devblo.account.command.openAccount;

import com.devblo.account.AccountId;
import com.devblo.account.AccountType;
import com.devblo.common.ICommand;
import com.devblo.common.Result;
import com.devblo.customer.CustomerId;

/**
 * Command to open a new bank account.
 *
 * @param customerId  The customer who owns this account
 * @param accountType Type of account (CHECKING, SAVINGS, etc.)
 * @param currency    Currency code (e.g., "TRY", "USD", "EUR")
 */
public record OpenAccountCommand(
        CustomerId customerId,
        AccountType accountType,
        String currency
) implements ICommand<Result<AccountId>> {
}

